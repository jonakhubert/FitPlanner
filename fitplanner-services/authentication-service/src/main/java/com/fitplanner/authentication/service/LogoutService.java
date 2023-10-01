package com.fitplanner.authentication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitplanner.authentication.model.api.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class LogoutService implements LogoutHandler {

    private final UserService userService;

    @Autowired
    public LogoutService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            final var authHeader = request.getHeader("Authorization");

            if(authHeader == null || !authHeader.startsWith("Bearer "))
                return;

            var token = authHeader.substring(7);
            userService.revokeAccessToken(token);
        } catch(RuntimeException ex){
            handleUserNotFoundException(ex, request, response);
        }
    }

    private void handleUserNotFoundException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        var apiError = new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.UNAUTHORIZED.value(),
            LocalDateTime.now().toString()
        );

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        var objectMapper = new ObjectMapper();
        try {
            response.getWriter().write(objectMapper.writeValueAsString(apiError));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
