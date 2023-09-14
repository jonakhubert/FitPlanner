package com.fitplanner.authentication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitplanner.authentication.exception.model.UserNotFoundException;
import com.fitplanner.authentication.model.api.ApiError;
import com.fitplanner.authentication.service.JwtService;
import com.fitplanner.authentication.model.accesstoken.AccessToken;
import com.fitplanner.authentication.repository.AccessTokenRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final AccessTokenRepository accessTokenRepository;

    @Autowired
    public JwtAuthenticationFilter(
        JwtService jwtService,
        UserDetailsService userDetailsService,
        AccessTokenRepository accessTokenRepository
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.accessTokenRepository = accessTokenRepository;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            final var authenticationHeader = request.getHeader("Authorization");

            // check if authentication token is missing
            if(authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            var jwt = authenticationHeader.substring(7); // index 7 because of 'Bearer '
            var username = jwtService.extractUsername(jwt);

            // check if user is authenticated (connected)
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var userDetails = userDetailsService.loadUserByUsername(username);

                var token = accessTokenRepository.findByToken(jwt)
                    .orElse(null);

                if(jwtService.isTokenValid(jwt, userDetails) && token != null) {
                    var authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    ); // used to update security context

                    authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch(JwtException | UserNotFoundException ex) {
            handleJwtException(ex, request, response);
        }
    }

    private void handleJwtException(
        Exception ex,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        var apiError = new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.UNAUTHORIZED.value(),
            LocalDateTime.now().toString()
        );

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        var objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiError));
    }
}
