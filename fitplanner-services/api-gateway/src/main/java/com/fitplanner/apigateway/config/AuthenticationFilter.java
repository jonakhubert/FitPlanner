package com.fitplanner.apigateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RestTemplate restTemplate;

    @Autowired
    public AuthenticationFilter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            List<String> authHeaderList = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

            if (authHeaderList == null || authHeaderList.isEmpty())
                throw new RuntimeException("Missing auth information");

            String authHeader = authHeaderList.get(0);

            if (authHeader == null || !authHeader.startsWith("Bearer "))
                throw new RuntimeException("Invalid authentication");

            String token = authHeader.substring(7);

            ResponseEntity<Void> response = restTemplate.postForEntity(
                "http://authentication-service/api/auth/validate-token",
                null,
                Void.class,
                "Bearer " + token
            );

            if (response.getStatusCode().is2xxSuccessful())
                return chain.filter(exchange);
            else {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        });
    }

    public static class Config {}
}
