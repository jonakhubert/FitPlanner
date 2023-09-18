package com.fitplanner.authentication.service;

import com.fitplanner.authentication.exception.model.*;
import com.fitplanner.authentication.model.ResetPasswordToken;
import com.fitplanner.authentication.model.VerificationToken;
import com.fitplanner.authentication.model.api.LoginRequest;
import com.fitplanner.authentication.model.api.RegisterRequest;
import com.fitplanner.authentication.model.accesstoken.AccessToken;
import com.fitplanner.authentication.model.api.ResetPasswordRequest;
import com.fitplanner.authentication.model.user.User;
import com.fitplanner.authentication.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
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
    private TokenService tokenService;
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
        var email = "any@gmail.com";
        var registerRequest = new RegisterRequest("any", "any", email, "any");
        var token = new VerificationToken("token", null, null, email);
        var user = new User("", "", email, "", null);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);
        when(tokenService.createVerificationToken(eq(email))).thenReturn(token);

        // when
        var response = underTest.register(registerRequest);

        // then
        assertNotNull(response);
        assertEquals("Verification email has been sent.", response.message());
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(userRepository, times(1)).save(any(User.class));
        verify(tokenService, times(1)).createVerificationToken(eq(email));
        verify(emailService, times(1)).send(eq(registerRequest.email()), anyString(), anyString());
    }

    @Test
    public void register_RegisterRequestWithExistingEmail_UserAlreadyExistException() {
        // given
        var registerRequest = new RegisterRequest("any", "any", "valid@gmail.com", "any");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        // then
        assertThrows(UserAlreadyExistException.class, () -> underTest.register(registerRequest));
        verify(userRepository, times(1)).findByEmail(eq(registerRequest.email()));
        verify(tokenService, never()).createVerificationToken(anyString());
        verify(emailService, never()).send(anyString(), anyString(), anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void register_RegisterRequestWithInvalidEmail_InvalidEmailFormatException() {
        // given
        var registerRequest = new RegisterRequest("any", "any", "invalid", "any");

        // then
        assertThrows(InvalidEmailFormatException.class, () -> underTest.register(registerRequest));
        verify(userRepository, never()).findByEmail(eq(registerRequest.email()));
        verify(userRepository, never()).save(any(User.class));
        verify(tokenService, never()).createVerificationToken(anyString());
        verify(emailService, never()).send(anyString(), anyString(), anyString());
    }

    @Test
    public void login_LoginRequestWithVerifiedEmail_LoginResponseWithAccessToken() {
        // given
        var token = "token";
        var email = "any@gmail.com";
        var loginRequest = new LoginRequest(email, "any");
        var user = new User("", "", email, "", null);
        user.setEnabled(true);

        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(jwtService.generateToken(any())).thenReturn(token);

        // when
        var result = underTest.login(loginRequest);

        // then
        assertNotNull(result);
        assertEquals(token, result.accessToken());
        verify(userRepository, times(1)).findByEmail(eq(loginRequest.email()));
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtService, times(1)).generateToken(eq(user));
        verify(tokenService, times(1)).deleteAccessToken(eq(email));
        verify(tokenService, times(1)).createAccessToken(eq(loginRequest.email()), eq(token));
    }

    @Test
    public void login_LoginRequestWithNotVerifiedEmail_UserNotVerifiedException() {
        // given
        var email = "any@gmail.com";
        var loginRequest = new LoginRequest(email, "any");
        var user = new User("", "", email, "", null);

        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(user));
        when(tokenService.createVerificationToken(anyString())).thenReturn(new VerificationToken());

        // then
        assertThrows(UserNotVerifiedException.class, () -> underTest.login(loginRequest));
        verify(userRepository, times(1)).findByEmail(eq(loginRequest.email()));
        verify(tokenService, times(1)).deleteVerificationToken(eq(loginRequest.email()));
        verify(emailService, times(1)).send(anyString(), anyString(), anyString());
        verify(authenticationManager, never()).authenticate(any());
        verify(jwtService, never()).generateToken(any(User.class));
        verify(tokenService, never()).deleteAccessToken(anyString());
        verify(tokenService, never()).createAccessToken(anyString(), anyString());
    }

    @Test
    public void login_LoginRequestWithInvalidEmail_InvalidEmailFormatException() {
        // given
        var loginRequest = new LoginRequest("invalid", "any");

        // then
        assertThrows(InvalidEmailFormatException.class, () -> underTest.login(loginRequest));
        verify(userRepository, never()).findByEmail(anyString());
        verify(authenticationManager, never()).authenticate(any());
        verify(jwtService, never()).generateToken(any(User.class));
        verify(tokenService, never()).deleteAccessToken(anyString());
        verify(tokenService, never()).createAccessToken(anyString(), anyString());
    }

    @Test
    public void login_LoginRequestWithNonExistingEmail_UserNotFoundException() {
        // given
        var loginRequest = new LoginRequest("any@gmail.com", "any");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // then
        assertThrows(UserNotFoundException.class, () -> underTest.login(loginRequest));
        verify(userRepository, times(1)).findByEmail(eq(loginRequest.email()));
        verify(authenticationManager, never()).authenticate(any());
        verify(jwtService, never()).generateToken(any(User.class));
        verify(tokenService, never()).deleteAccessToken(anyString());
        verify(tokenService, never()).createAccessToken(anyString(), anyString());
    }

    @Test
    public void isAccessTokenValid_ValidToken_True() {
        // given
        var token = "token";

        when(tokenService.isAccessTokenValid(token)).thenReturn(true);

        // when
        var result = underTest.isAccessTokenValid(token);

        // then
        assertTrue(result);
    }

    @Test
    public void isAccessTokenValid_InvalidToken_False() {
        // given
        var token = "token";

        when(tokenService.isAccessTokenValid(token)).thenReturn(false);

        // when
        var result = underTest.isAccessTokenValid(token);

        // then
        assertFalse(result);
    }

    @Test
    public void verify_NotConfirmedToken_ConfirmationMessage() {
        // given
        var token = "token";
        var email = "any@gmail.com";
        var verificationToken = new VerificationToken(
            token,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(5),
            email
        );

        when(tokenService.findVerificationToken(token)).thenReturn(verificationToken);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        // when
        var result = underTest.verify(token);

        // then
        assertEquals(result.message(), "User account verified.");
        verify(tokenService, times(1)).findVerificationToken(eq(token));
        verify(tokenService, times(1)).saveVerificationToken(eq(verificationToken));
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void verify_ConfirmedToken_UserAlreadyVerifiedException() {
        // given
        var verificationToken = new VerificationToken();
        verificationToken.setConfirmedAt(LocalDateTime.now());

        when(tokenService.findVerificationToken(anyString())).thenReturn(verificationToken);

        // then
        assertThrows(UserAlreadyVerifiedException.class, () -> underTest.verify("token"));
        verify(tokenService, times(1)).findVerificationToken(anyString());
        verify(tokenService, never()).saveVerificationToken(any(VerificationToken.class));
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void verify_ExpiredToken_TokenExpiredException() {
        // given
        var token = "token";
        var email = "any@gmail.com";
        var verificationToken = new VerificationToken(
            token,
            LocalDateTime.now().minusMinutes(10),
            LocalDateTime.now().minusMinutes(5),
            email
        );

        when(tokenService.findVerificationToken(anyString())).thenReturn(verificationToken);

        // then
        assertThrows(TokenExpiredException.class, () -> underTest.verify(token));
        verify(tokenService, times(1)).findVerificationToken(eq(token));
        verify(tokenService, never()).saveVerificationToken(any(VerificationToken.class));
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void forgotPassword_ExistingUser_ConfirmationMessage() {
        // given
        var email = "any@gmail.com";
        var user = new User("", "", email, "", null);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(tokenService.createResetPasswordToken(email)).thenReturn(new ResetPasswordToken());

        // when
        var result = underTest.forgotPassword(email);

        // then
        assertNotNull(result);
        assertEquals(result.message(), "Reset password has been sent.");
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(tokenService, times(1)).deleteResetPasswordToken(eq(email));
        verify(tokenService, times(1)).createResetPasswordToken(eq(email));
        verify(emailService, times(1)).send(anyString(), anyString(),anyString());
    }

    @Test
    public void forgotPassword_NonExistingUser_UserNotFoundException() {
        // given
        var email = "any@gmail.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // then
        assertThrows(UserNotFoundException.class, () -> underTest.forgotPassword(email));
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(tokenService, never()).deleteResetPasswordToken(anyString());
        verify(tokenService, never()).createResetPasswordToken(anyString());
        verify(emailService, never()).send(anyString(), anyString(),anyString());
    }

    @Test
    public void resetPassword_ExistingUserAndNotExpiredToken_ConfirmationMessage() {
        // given
        var email = "any@gmail.com";
        var token = "token";
        var request = new ResetPasswordRequest(email, token, "password");
        var user = new User("", "", email, "", null);
        var resetPasswordToken = new ResetPasswordToken(token, null, LocalDateTime.now().plusMinutes(5), email);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(tokenService.findResetPasswordToken(token)).thenReturn(resetPasswordToken);

        // when
        var result = underTest.resetPassword(request);

        // then
        assertNotNull(result);
        assertEquals(result.message(), "Password reset successfully.");
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(tokenService, times(1)).findResetPasswordToken(eq(token));
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(eq(user));
        verify(tokenService, times(1)).deleteResetPasswordToken(eq(email));
        verify(tokenService, times(1)).deleteAccessToken(eq(email));
    }

    @Test
    public void resetPassword_NonExistingUser_UserNotFoundException() {
        // given
        var email = "any@gmail.com";
        var token = "token";
        var request = new ResetPasswordRequest(email, token, "password");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        // then
        assertThrows(UserNotFoundException.class, () -> underTest.resetPassword(request));
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(tokenService, never()).findResetPasswordToken(eq(token));
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(tokenService, never()).deleteResetPasswordToken(eq(email));
        verify(tokenService, never()).deleteAccessToken(eq(email));
    }

    @Test
    public void resetPassword_ExistingUserAndExpiredToken_TokenExpiredException() {
        // given
        var email = "any@gmail.com";
        var token = "token";
        var request = new ResetPasswordRequest(email, token, "password");
        var user = new User("", "", email, "", null);
        var resetPasswordToken = new ResetPasswordToken(token, null, LocalDateTime.now().minusMinutes(5), email);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(tokenService.findResetPasswordToken(token)).thenReturn(resetPasswordToken);

        // then
        assertThrows(TokenExpiredException.class, () -> underTest.resetPassword(request));
        verify(userRepository, times(1)).findByEmail(eq(email));
        verify(tokenService, times(1)).findResetPasswordToken(eq(token));
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(eq(user));
        verify(tokenService, never()).deleteResetPasswordToken(eq(email));
        verify(tokenService, never()).deleteAccessToken(eq(email));
    }

    @Test
    public void isResetPasswordTokenValid_ValidToken_True() {
        // given
        var token = "valid-token";

        when(tokenService.isResetPasswordTokenValid(token)).thenReturn(true);

        // when
        var response = underTest.isResetPasswordTokenValid(token);

        // then
        assertTrue(response);
        verify(tokenService, times(1)).isResetPasswordTokenValid(eq(token));
    }

    @Test
    public void isTokenValid_InvalidToken_False() {
        // given
        var token = "invalid-token";

        when(tokenService.isResetPasswordTokenValid(token)).thenReturn(false);

        // when
        var response = underTest.isResetPasswordTokenValid(token);

        // then
        assertFalse(response);
        verify(tokenService, times(1)).isResetPasswordTokenValid(eq(token));
    }
}
