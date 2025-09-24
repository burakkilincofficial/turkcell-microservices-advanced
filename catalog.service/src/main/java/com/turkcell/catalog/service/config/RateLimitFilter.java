package com.turkcell.catalog.service.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    // INMemory
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Value("${spring.data.redis.uri:redis://localhost:6379}")
    private String redisUri;

    private LettuceBasedProxyManager<String> proxyManager;

    @PostConstruct
    void init() {
        RedisClient redisClient = RedisClient.create(redisUri);
        StatefulRedisConnection<String, byte[]> conn = redisClient
                .connect(RedisCodec.of(new StringCodec(), new ByteArrayCodec()));
        proxyManager = LettuceBasedProxyManager
                .builderFor(conn, key -> ("bucket4j:"+key).getBytes())
                .build();
        System.out.println("Redis bağlantısı başarılı.");
    }

    private BucketConfiguration newBucket() {
        Bandwidth limitPerMinute = Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1)));
        Bandwidth burst = Bandwidth.classic(20, Refill.intervally(20, Duration.ofMinutes(1)));
        return BucketConfiguration.builder().addLimit(limitPerMinute).addLimit(burst).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }

        Bucket bucket = proxyManager
                .builder()
                .build(ip, (Function<String,BucketConfiguration>) k -> newBucket());

        //buckets.computeIfAbsent(ip, k -> newBucket());

        if(bucket.tryConsume(1))
        {
            filterChain.doFilter(request, response);
        }else{
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setHeader("Retry-After", "60");
            response.getWriter().write("Too Many Requests");
        }
    }
}
