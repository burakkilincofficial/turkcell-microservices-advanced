package com.turkcell.catalog.service.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
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
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.function.Supplier;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    @Value("${spring.data.redis.uri:redis://localhost:6379}")
    private String redisUri;

    private LettuceBasedProxyManager<byte[]> proxyManager;

    @PostConstruct
    public void init() {
        RedisClient redisClient = RedisClient.create(redisUri);
        StatefulRedisConnection<byte[], byte[]> connection = redisClient.connect(new ByteArrayCodec());
        this.proxyManager = LettuceBasedProxyManager.builderFor(connection).build();
    }

    private Supplier<BucketConfiguration> newBucketConfig() {
        return () -> {
            Bandwidth limitPerMinute = Bandwidth.simple(10, Duration.ofMinutes(1));
            Bandwidth burst = Bandwidth.simple(20, Duration.ofMinutes(1));
            return BucketConfiguration.builder()
                    .addLimit(limitPerMinute)
                    .addLimit(burst)
                    .build();
        };
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String ip = getClientIp(request);
        byte[] key = ("bucket4j:" + ip).getBytes(StandardCharsets.UTF_8);

        Bucket bucket = this.proxyManager.builder().build(key, newBucketConfig());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setHeader("Retry-After", "60");
            response.getWriter().write("Too Many Requests");
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-FORWARDED-FOR");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
