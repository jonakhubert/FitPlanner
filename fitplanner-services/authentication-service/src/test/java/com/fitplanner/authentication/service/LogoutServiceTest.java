package com.fitplanner.authentication.service;

import com.fitplanner.authentication.exception.model.UserNotFoundException;
import com.fitplanner.authentication.model.api.ApiError;
import com.fitplanner.authentication.model.tokens.accesstoken.AccessToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogoutServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Authentication authentication;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private LogoutService underTest;

    @Test
    public void logout_ValidAuthorizationHeader_RevokeAccessToken() {
        // given
        var validToken = "validToken";
        var authHeader = "Bearer " + validToken;

        when(request.getHeader("Authorization")).thenReturn(authHeader);

        // when
        underTest.logout(request, response, authentication);

        // then
        verify(userService, times(1)).revokeAccessToken(eq(validToken));
    }

    @Test
    public void logout_NoAuthorizationHeader_NoRevoking() {
        // given
        when(request.getHeader("Authorization")).thenReturn(null);

        // when
        underTest.logout(request, response, authentication);

        // then
        verify(userService, never()).revokeAccessToken(any());
    }

    @Test
    public void logout_InvalidAuthorizationHeader_NoRevoking() {
        // given
        var invalidToken = "invalidToken";
        var authHeader = "Invalid " + invalidToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);

        // when
        underTest.logout(request, response, authentication);

        // then
        verify(userService, never()).revokeAccessToken(any());
    }

    @Test
    public void logout_InvalidAccessToken_UserNotFoundException() throws IOException {
        // given
        var invalidToken = "invalidToken";
        var authHeader = "Bearer " + invalidToken;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(response.getWriter()).thenReturn(mock(PrintWriter.class));
        doThrow(new UserNotFoundException("User not found.")).when(userService).revokeAccessToken(invalidToken);

        // when
        underTest.logout(request, response, authentication);

        // then
        verify(userService, times(1)).revokeAccessToken(any());
    }
}
