package com.turkcell.orderservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignClientInterceptor {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                // http isteğinin kendisi
                ServletRequestAttributes attrs = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

                if(attrs != null)
                {
                    HttpServletRequest req = attrs.getRequest();
                    String authHeader = req.getHeader("Authorization");
                    if(authHeader != null && !authHeader.isBlank())
                    {
                        requestTemplate.header("Authorization", authHeader);
                    }
                }
            }
        };
    }
}
