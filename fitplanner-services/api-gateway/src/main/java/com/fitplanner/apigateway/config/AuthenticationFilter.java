package com.fitplanner.apigateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

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

            String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders()
                    .get(HttpHeaders.AUTHORIZATION)).get(0);

            if (authHeader == null || !authHeader.startsWith("Bearer "))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

            String token = authHeader.substring(7);

            return webClientBuilder.build()
                .post()
                .uri("http://localhost:8090/api/auth/validate-token")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful())
                        return chain.filter(exchange);
                    else {
                        // if it's not successful, let the error pass through
                        return clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                            exchange.getResponse().setStatusCode(clientResponse.statusCode());
                            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                                .bufferFactory().wrap(errorBody.getBytes())));
                        });
                    }
                });
        });
    }

    public static class Config {}
}
