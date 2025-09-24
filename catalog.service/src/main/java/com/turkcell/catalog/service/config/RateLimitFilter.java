package com.turkcell.catalog.service.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();


    private Bucket newBucket() {
        Bandwidth limitPerMinute = Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1)));
        Bandwidth burst = Bandwidth.classic(20, Refill.intervally(20, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limitPerMinute).addLimit(burst).build();
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

        Bucket bucket = buckets.computeIfAbsent(ip, k -> newBucket());

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
