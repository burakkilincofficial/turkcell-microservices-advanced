package com.turkcell.catalog.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig
{
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasAnyAuthority("ADMIN")
                        .anyRequest().authenticated()
                ).oauth2ResourceServer(
                        oauth -> oauth.jwt(
                                jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(keyCloakAuthenticationConverter())
                        )
                );
        return http.build();
    }

    @Bean
    JwtAuthenticationConverter keyCloakAuthenticationConverter() {
        JwtAuthenticationConverter keyCloakAuthenticationConverter = new JwtAuthenticationConverter();
        keyCloakAuthenticationConverter.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);
        return keyCloakAuthenticationConverter;
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt)
    {
        Set<String> roles = new HashSet<>();

        Map<String,Object> realmAccess = jwt.getClaim("realm_access");
        if(realmAccess != null)
        {
            Object realmRoles = realmAccess.get("roles");
            if(realmRoles != null && realmRoles instanceof Collection)
                ((Collection<?>) realmRoles).forEach(r -> roles.add(String.valueOf(r)));
        }

        Map<String,Object> resourceAccess = jwt.getClaim("resource_access");
        if(resourceAccess != null)
        {
            Map<String,Object> account = (Map<String, Object>) realmAccess.get("account");
            if(account != null)
            {
                Object accountRoles = account.get("roles");
                ((Collection<?>) accountRoles).forEach(r -> roles.add(String.valueOf(r)));
            }
        }

        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

}
