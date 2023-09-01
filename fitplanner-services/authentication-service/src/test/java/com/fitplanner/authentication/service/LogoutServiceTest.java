package com.fitplanner.authentication.service;

import com.fitplanner.authentication.model.token.Token;
import com.fitplanner.authentication.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogoutServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LogoutService underTest;

    @Test
    public void shouldDeleteTokenWhenValidAuthorizationHeaderIsPresent() {
        // given
        String validToken = "validToken";
        String authHeader = "Bearer " + validToken;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenRepository.findByToken(validToken)).thenReturn(Optional.of(new Token()));

        // when
        underTest.logout(request, response, authentication);

        // then
        verify(tokenRepository, times(1)).delete(any());
    }

    @Test
    public void shouldNotDeleteTokenWhenAuthorizationHeaderIsNotPresent() {
        // given
        when(request.getHeader("Authorization")).thenReturn(null);

        // when
        underTest.logout(request, response, authentication);

        // then
        verify(tokenRepository, never()).delete(any());
    }

    @Test
    public void shouldNotDeleteTokenWhenInvalidAuthorizationHeaderIsPresent() {
        // given
        String invalidToken = "invalidToken";
        String authHeader = "Invalid " + invalidToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);

        // when
        underTest.logout(request, response, authentication);

        // then
        verify(tokenRepository, never()).delete(any());
    }
}
