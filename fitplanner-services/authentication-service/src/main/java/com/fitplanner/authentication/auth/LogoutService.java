package com.fitplanner.authentication.auth;

import com.fitplanner.authentication.exception.InvalidAuthenticationTokenException;
import com.fitplanner.authentication.exception.TokenNotFoundException;
import com.fitplanner.authentication.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Autowired
    public LogoutService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String token;

        if(authHeader == null || !authHeader.startsWith("Bearer "))
            throw new InvalidAuthenticationTokenException("Invalid authentication token.");

        token = authHeader.substring(7);
        var storedToken = tokenRepository.findByToken(token)
            .orElseThrow(() -> new TokenNotFoundException("Token not found."));

        tokenRepository.delete(storedToken);
    }
}
