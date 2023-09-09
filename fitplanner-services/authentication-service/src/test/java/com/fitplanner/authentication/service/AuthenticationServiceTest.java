package com.fitplanner.authentication.service;

import com.fitplanner.authentication.exception.model.*;
import com.fitplanner.authentication.model.api.ConfirmationResponse;
import com.fitplanner.authentication.model.api.LoginRequest;
import com.fitplanner.authentication.model.api.LoginResponse;
import com.fitplanner.authentication.model.api.RegisterRequest;
import com.fitplanner.authentication.model.accesstoken.AccessToken;
import com.fitplanner.authentication.model.confirmationtoken.ConfirmationToken;
import com.fitplanner.authentication.model.user.User;
import com.fitplanner.authentication.repository.AccessTokenRepository;
import com.fitplanner.authentication.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AccessTokenService accessTokenService;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private EmailService emailService;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService underTest;

    @Test
    public void register_RegisterRequestWithNonExistingEmail_ConfirmationMessage() {
        // given
        RegisterRequest registerRequest = new RegisterRequest("any", "any", "valid@gmail.com", "any");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User());

        // when
        ConfirmationResponse response = underTest.register(registerRequest);

        // then
        assertNotNull(response);
        assertEquals("Verification email has been sent.", response.message());
        verify(emailService, times(1)).send(eq(registerRequest.email()), anyString());
        verify(userRepository, times(1)).save(any(User.class));
        verify(confirmationTokenService, times(1)).saveToken(any(ConfirmationToken.class));
    }

    @Test
    public void register_RegisterRequestWithExistingEmailAndEnabledUser_UserAlreadyExistException() {
        // given
        RegisterRequest registerRequest = new RegisterRequest("any", "any", "valid@gmail.com", "any");
        User user = new User();
        user.setEnabled(true);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // then
        assertThrows(UserAlreadyExistException.class, () -> underTest.register(registerRequest));
        verify(userRepository, times(1)).findByEmail(eq(registerRequest.email()));
    }

    @Test
    public void register_RegisterRequestWithExistingEmailAndNotEnabledUser_ConfirmationMessage() {
        // given
        RegisterRequest registerRequest = new RegisterRequest("any", "any", "valid@gmail.com", "any");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        // when
        ConfirmationResponse result = underTest.register(registerRequest);

        // then
        assertEquals(result.message(), "Confirmation email has been resend.");
        verify(emailService, times(1)).send(eq(registerRequest.email()), anyString());
        verify(confirmationTokenService, times(1)).deleteToken(eq(registerRequest.email()));
        verify(confirmationTokenService, times(1)).saveToken(any(ConfirmationToken.class));
    }

    @Test
    public void register_RegisterRequestWithInvalidEmail_InvalidEmailFormatException() {
        // given
        RegisterRequest registerRequest = new RegisterRequest("any", "any", "invalid", "any");

        // then
        assertThrows(InvalidEmailFormatException.class, () -> underTest.register(registerRequest));
        verify(userRepository, never()).findByEmail(eq(registerRequest.email()));
        verify(emailService, never()).send(eq(registerRequest.email()), anyString());
        verify(confirmationTokenService, never()).deleteToken(eq(registerRequest.email()));
        verify(userRepository, never()).save(any());
        verify(confirmationTokenService, never()).saveToken(any(ConfirmationToken.class));
    }

    @Test
    public void login_LoginRequestWithVerifiedEmail_AuthenticationResponseWithAccessToken() {
        // given
        LoginRequest loginRequest = new LoginRequest("any@email.com", "any");
        User user = new User();
        user.setEnabled(true);

        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(jwtService.generateToken(any())).thenReturn("token");

        // when
        LoginResponse result = underTest.login(loginRequest);

        // then
        assertNotNull(result);
        assertEquals("token", result.accessToken());
        verify(userRepository, times(1)).findByEmail(eq(loginRequest.email()));
        verify(jwtService, times(1)).generateToken(eq(user));
        verify(accessTokenService, times(1)).deleteToken(eq(user));
        verify(accessTokenService, times(1)).saveToken(any(AccessToken.class));
    }

    @Test
    public void login_LoginRequestWithNotVerifiedEmail_UserNotVerifiedException() {
        // given
        LoginRequest loginRequest = new LoginRequest("any@email.com", "any");

        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(new User()));

        // then
        assertThrows(UserNotVerifiedException.class, () -> underTest.login(loginRequest));
        verify(userRepository, times(1)).findByEmail(eq(loginRequest.email()));
        verify(authenticationManager, never()).authenticate(any());
        verify(jwtService, never()).generateToken(any(User.class));
        verify(accessTokenService, never()).deleteToken(any(User.class));
        verify(accessTokenService, never()).saveToken(any(AccessToken.class));
    }

    @Test
    public void login_LoginRequestWithInvalidEmail_InvalidEmailFormatException() {
        // given
        LoginRequest loginRequest = new LoginRequest("invalid", "any");

        // then
        assertThrows(InvalidEmailFormatException.class, () -> underTest.login(loginRequest));
        verify(userRepository, never()).findByEmail(anyString());
        verify(authenticationManager, never()).authenticate(any());
        verify(jwtService, never()).generateToken(any(User.class));
        verify(accessTokenService, never()).deleteToken(any(User.class));
        verify(accessTokenService, never()).saveToken(any(AccessToken.class));
    }

    @Test
    public void login_LoginRequestWithNonExistingEmail_UserNotFoundException() {
        // given
        LoginRequest loginRequest = new LoginRequest("any@gmail.com", "any");

        // then
        assertThrows(UserNotFoundException.class, () -> underTest.login(loginRequest));
        verify(userRepository, times(1)).findByEmail(eq(loginRequest.email()));
        verify(authenticationManager, never()).authenticate(any());
        verify(jwtService, never()).generateToken(any(User.class));
        verify(accessTokenService, never()).deleteToken(any(User.class));
        verify(accessTokenService, never()).saveToken(any(AccessToken.class));
    }

    @Test
    public void verify_NotConfirmedToken_ConfirmationMessage() {
        // given
        String token = "token";
        String email = "any@gmail.com";
        ConfirmationToken confirmationToken = new ConfirmationToken(
            token,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(5),
            email
        );

        when(confirmationTokenService.getToken(token)).thenReturn(confirmationToken);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        // when
        ConfirmationResponse result = underTest.verify(token);

        // then
        assertEquals(result.message(), "User account verified.");
        verify(confirmationTokenService, times(1)).setConfirmedAt(anyString());
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void verify_ConfirmedToken_UserAlreadyVerifiedException() {
        // given
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setConfirmedAt(LocalDateTime.now());

        when(confirmationTokenService.getToken(anyString())).thenReturn(confirmationToken);

        // then
        assertThrows(UserAlreadyVerifiedException.class, () -> underTest.verify("token"));
        verify(confirmationTokenService, times(1)).getToken(anyString());
        verify(confirmationTokenService, never()).setConfirmedAt(anyString());
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void verify_ExpiredToken_TokenExpiredException() {
        // given
        String token = "token";
        String email = "any@gmail.com";
        ConfirmationToken confirmationToken = new ConfirmationToken(
            token,
            LocalDateTime.now().minusMinutes(10),
            LocalDateTime.now().minusMinutes(5),
            email
        );

        when(confirmationTokenService.getToken(anyString())).thenReturn(confirmationToken);

        // then
        assertThrows(TokenExpiredException.class, () -> underTest.verify(token));
        verify(confirmationTokenService, times(1)).getToken(anyString());
        verify(confirmationTokenService, never()).setConfirmedAt(anyString());
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void isTokenValid_ValidToken_True() {
        // given
        String token = "valid-token";

        when(accessTokenService.isTokenValid(token)).thenReturn(true);

        // when
        boolean response = underTest.isTokenValid(token);

        // then
        assertTrue(response);
        verify(accessTokenService, times(1)).isTokenValid(eq(token));
    }

    @Test
    public void isTokenValid_InvalidToken_False() {
        // given
        String token = "invalid-token";

        when(accessTokenService.isTokenValid(token)).thenReturn(false);

        // when
        boolean response = underTest.isTokenValid(token);

        // then
        assertFalse(response);
        verify(accessTokenService, times(1)).isTokenValid(eq(token));
    }
}
