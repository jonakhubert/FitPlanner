package com.fitplanner.authentication.service;

import com.fitplanner.authentication.repository.AccessTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {

    private final AccessTokenRepository accessTokenRepository;

    @Autowired
    public LogoutService(AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String token;

        if(authHeader == null || !authHeader.startsWith("Bearer "))
           return;

        token = authHeader.substring(7);
        accessTokenRepository.findByToken(token).ifPresent(accessTokenRepository::delete);
    }
}
