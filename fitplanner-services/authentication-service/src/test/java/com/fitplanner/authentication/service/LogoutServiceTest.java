package com.fitplanner.authentication.service;

import com.fitplanner.authentication.model.accesstoken.AccessToken;
import com.fitplanner.authentication.repository.AccessTokenRepository;
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
    private AccessTokenRepository accessTokenRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private LogoutService underTest;

    @Test
    public void logout_ValidAuthorizationHeader_DeleteAccessToken() {
        // given
        String validToken = "validToken";
        String authHeader = "Bearer " + validToken;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(accessTokenRepository.findByToken(validToken)).thenReturn(Optional.of(new AccessToken()));

        // when
        underTest.logout(request, response, authentication);

        // then
        verify(accessTokenRepository, times(1)).delete(any());
    }

    @Test
    public void logout_NoAuthorizationHeader_NoAccessTokenRemoval() {
        // given
        when(request.getHeader("Authorization")).thenReturn(null);

        // when
        underTest.logout(request, response, authentication);

        // then
        verify(accessTokenRepository, never()).delete(any());
    }

    @Test
    public void logout_InvalidAuthorizationHeader_NoAccessTokenRemoval() {
        // given
        String invalidToken = "invalidToken";
        String authHeader = "Invalid " + invalidToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);

        // when
        underTest.logout(request, response, authentication);

        // then
        verify(accessTokenRepository, never()).delete(any());
    }
}
