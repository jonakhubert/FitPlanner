package com.fitplanner.apigateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public AuthenticationFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

            var authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders()
                .get(HttpHeaders.AUTHORIZATION)).get(0);

            if (authHeader == null || !authHeader.startsWith("Bearer "))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

            var token = authHeader.substring(7);

            return webClientBuilder.build()
                .post()
                .uri("http://localhost:8222/api/user-authentication/access-tokens")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful())
                        return chain.filter(exchange);
                    else
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                });
        });
    }

    public static class Config {}
}
