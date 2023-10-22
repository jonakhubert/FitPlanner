package com.fitplanner.authentication.service;

import com.fitplanner.authentication.exception.model.*;
import com.fitplanner.authentication.model.tokens.ResetPasswordToken;
import com.fitplanner.authentication.model.tokens.VerificationToken;
import com.fitplanner.authentication.model.api.LoginRequest;
import com.fitplanner.authentication.model.api.RegisterRequest;
import com.fitplanner.authentication.model.api.ResetPasswordRequest;
import com.fitplanner.authentication.model.tokens.accesstoken.AccessToken;
import com.fitplanner.authentication.model.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private EmailService emailService;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService underTest;

    @Test
    public void register_RegisterRequestWithNonExistingEmail_ConfirmationMessage() {
        // given
        var email = "any@gmail.com";
        var request = new RegisterRequest("any", "any", 191.0, 88.0, 1, 2, email, "any");
        var token = new VerificationToken("token", null, null);
        var user = new User("", "", email, "", null);

        when(userService.isUserExist(email)).thenReturn(false);
        when(userService.createUser(request)).thenReturn(user);
        when(userService.createVerificationToken(user)).thenReturn(token);

        // when
        var response = underTest.register(request);

        // then
        assertNotNull(response);
        assertEquals("Verification email has been sent.", response.message());
        verify(userService, times(1)).isUserExist(eq(email));
        verify(userService, times(1)).createUser(eq(request));
        verify(userService, times(1)).createVerificationToken(eq(user));
        verify(emailService, times(1)).send(eq(request.email()), anyString(), anyString());
    }

    @Test
    public void register_RegisterRequestWithExistingEmail_UserAlreadyExistException() {
        // given
        var request = new RegisterRequest("any", "any", 191.0, 88.0, 1, 2, "valid@gmail.com", "any");

        when(userService.isUserExist(request.email())).thenReturn(true);

        // then
        assertThrows(UserAlreadyExistException.class, () -> underTest.register(request));
        verify(userService, times(1)).isUserExist(eq(request.email()));
        verify(userService, never()).createUser(eq(request));
        verify(userService, never()).createVerificationToken(any(User.class));
        verify(emailService, never()).send(eq(request.email()), anyString(), anyString());
    }

    @Test
    public void register_RegisterRequestWithInvalidEmail_InvalidEmailFormatException() {
        // given
        var request = new RegisterRequest("any", "any", 191.0, 88.0, 1, 2, "invalid", "any");

        // then
        assertThrows(InvalidEmailFormatException.class, () -> underTest.register(request));
        verify(userService, never()).isUserExist(eq(request.email()));
        verify(userService, never()).createUser(eq(request));
        verify(userService, never()).createVerificationToken(any(User.class));
        verify(emailService, never()).send(eq(request.email()), anyString(), anyString());
    }

    @Test
    public void login_LoginRequestWithVerifiedEmail_LoginResponseWithAccessToken() {
        // given
        var token = "token";
        var email = "any@gmail.com";
        var loginRequest = new LoginRequest(email, "any");
        var user = new User("", "", email, "", null);
        var accessToken = new AccessToken(token);
        user.setEnabled(true);
        user.setAccessToken(accessToken);

        when(userService.getUserByEmail(loginRequest.email())).thenReturn(user);
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(jwtService.generateToken(any())).thenReturn(token);
        when(userService.createAccessToken(any(User.class), anyString())).thenReturn(accessToken);

        // when
        var result = underTest.login(loginRequest);

        // then
        assertNotNull(result);
        assertEquals(token, result.accessToken());
        verify(userService, times(1)).getUserByEmail(eq(loginRequest.email()));
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtService, times(1)).generateToken(eq(user));
        verify(userService, times(1)).createAccessToken(eq(user), anyString());
    }

    @Test
    public void login_LoginRequestWithNotVerifiedEmail_UserNotVerifiedException() {
        // given
        var email = "any@gmail.com";
        var loginRequest = new LoginRequest(email, "any");
        var user = new User("", "", email, "", null);

        when(userService.getUserByEmail(loginRequest.email())).thenReturn(user);
        when(userService.createVerificationToken(user)).thenReturn(new VerificationToken());

        // then
        assertThrows(UserNotVerifiedException.class, () -> underTest.login(loginRequest));
        verify(userService, times(1)).getUserByEmail(eq(loginRequest.email()));
        verify(userService, times(1)).createVerificationToken(eq(user));
        verify(emailService, times(1)).send(anyString(), anyString(), anyString());
        verify(authenticationManager, never()).authenticate(any());
        verify(jwtService, never()).generateToken(any(User.class));
        verify(userService, never()).createAccessToken(eq(user), anyString());
    }

    @Test
    public void login_LoginRequestWithInvalidEmail_InvalidEmailFormatException() {
        // given
        var loginRequest = new LoginRequest("invalid", "any");

        // then
        assertThrows(InvalidEmailFormatException.class, () -> underTest.login(loginRequest));
        verify(userService, never()).getUserByEmail(anyString());
        verify(authenticationManager, never()).authenticate(any());
        verify(jwtService, never()).generateToken(any(User.class));
        verify(userService, never()).createAccessToken(any(User.class), anyString());
    }

    @Test
    public void login_LoginRequestWithNonExistingEmail_UserNotFoundException() {
        // given
        var loginRequest = new LoginRequest("any@gmail.com", "any");

        when(userService.getUserByEmail(loginRequest.email())).thenThrow(new UserNotFoundException("User not found."));

        // then
        assertThrows(UserNotFoundException.class, () -> underTest.login(loginRequest));
        verify(userService, times(1)).getUserByEmail(eq(loginRequest.email()));
        verify(authenticationManager, never()).authenticate(any());
        verify(jwtService, never()).generateToken(any(User.class));
        verify(userService, never()).createAccessToken(any(User.class), anyString());
    }

    @Test
    public void isAccessTokenValid_ValidToken_True() {
        // given
        var token = "token";

        when(userService.isAccessTokenValid(token)).thenReturn(true);

        // when
        var result = underTest.isAccessTokenValid(token);

        // then
        assertTrue(result);
    }

    @Test
    public void isAccessTokenValid_InvalidToken_False() {
        // given
        var token = "token";

        when(userService.isAccessTokenValid(token)).thenReturn(false);

        // when
        var result = underTest.isAccessTokenValid(token);

        // then
        assertFalse(result);
    }

    @Test
    public void verify_NotConfirmedToken_ConfirmationMessage() {
        // given
        var token = "token";
        var verificationToken = new VerificationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5));
        var user = new User();
        user.setVerificationToken(verificationToken);

        when(userService.getVerificationToken(token)).thenReturn(verificationToken);
        when(userService.getUserByVerificationToken(token)).thenReturn(user);

        // when
        var result = underTest.verify(token);

        // then
        assertEquals(result.message(), "User account verified.");
        verify(userService, times(1)).getVerificationToken(eq(token));
        verify(userService, times(1)).getUserByVerificationToken(eq(token));
        verify(userService, times(1)).verifyUser(any(User.class));
    }

    @Test
    public void verify_ConfirmedToken_UserAlreadyVerifiedException() {
        // given
        var verificationToken = new VerificationToken();
        var user = new User();
        verificationToken.setConfirmedAt(LocalDateTime.now());
        user.setVerificationToken(verificationToken);

        when(userService.getVerificationToken(anyString())).thenReturn(verificationToken);

        // then
        assertThrows(UserAlreadyVerifiedException.class, () -> underTest.verify(anyString()));
        verify(userService, times(1)).getVerificationToken(anyString());
        verify(userService, never()).getUserByVerificationToken(anyString());
        verify(userService, never()).verifyUser(any(User.class));
    }

    @Test
    public void verify_ExpiredToken_TokenExpiredException() {
        // given
        var token = "token";
        var verificationToken = new VerificationToken(
            token,
            LocalDateTime.now().minusMinutes(10),
            LocalDateTime.now().minusMinutes(5)
        );
        var user = new User();
        user.setVerificationToken(verificationToken);

        when(userService.getVerificationToken(token)).thenReturn(verificationToken);

        // then
        assertThrows(TokenExpiredException.class, () -> underTest.verify(token));
        verify(userService, times(1)).getVerificationToken(eq(token));
        verify(userService, never()).getUserByVerificationToken(anyString());
        verify(userService, never()).verifyUser(any(User.class));
    }

    @Test
    public void forgotPassword_ExistingUser_ConfirmationMessage() {
        // given
        var email = "any@gmail.com";
        var user = new User("", "", email, "", null);

        when(userService.getUserByEmail(email)).thenReturn(user);
        when(userService.createResetPasswordToken(user)).thenReturn(new ResetPasswordToken());

        // when
        var result = underTest.forgotPassword(email);

        // then
        assertNotNull(result);
        assertEquals(result.message(), "Reset password has been sent.");
        verify(userService, times(1)).getUserByEmail(eq(email));
        verify(userService, times(1)).createResetPasswordToken(eq(user));
        verify(emailService, times(1)).send(anyString(), anyString(),anyString());
    }

    @Test
    public void forgotPassword_NonExistingUser_UserNotFoundException() {
        // given
        var email = "any@gmail.com";

        when(userService.getUserByEmail(email)).thenThrow(new UserNotFoundException("User not found."));

        // then
        assertThrows(UserNotFoundException.class, () -> underTest.forgotPassword(email));
        verify(userService, times(1)).getUserByEmail(eq(email));
        verify(userService,never()).createResetPasswordToken(any(User.class));
        verify(emailService, never()).send(anyString(), anyString(),anyString());
    }

    @Test
    public void forgotPassword_InvalidUserEmailFormat_InvalidEmailFormatException() {
        // given
        var email = "invalid";

        // then
        assertThrows(InvalidEmailFormatException.class, () -> underTest.forgotPassword(email));
        verify(userService, never()).getUserByEmail(eq(email));
        verify(userService,never()).createResetPasswordToken(any(User.class));
        verify(emailService, never()).send(anyString(), anyString(),anyString());
    }

    @Test
    public void resetPassword_ExistingUserAndNotExpiredToken_ConfirmationMessage() {
        // given
        var email = "any@gmail.com";
        var token = "token";
        var request = new ResetPasswordRequest(email, token, "password");
        var user = new User("", "", email, "", null);
        var resetPasswordToken = new ResetPasswordToken(token, null, LocalDateTime.now().plusMinutes(5));
        user.setResetPasswordToken(resetPasswordToken);

        when(userService.getUserByEmail(request.email())).thenReturn(user);

        // when
        var result = underTest.resetPassword(request);

        // then
        assertNotNull(result);
        assertEquals(result.message(), "Password reset successfully.");
        verify(userService, times(1)).getUserByEmail(eq(email));
        verify(userService, times(1)).resetPassword(eq(user), eq(request.newPassword()));
    }

    @Test
    public void resetPassword_NonExistingUser_UserNotFoundException() {
        // given
        var email = "any@gmail.com";
        var token = "token";
        var request = new ResetPasswordRequest(email, token, "password");

        when(userService.getUserByEmail(request.email())).thenThrow(new UserNotFoundException("User not found."));

        // then
        assertThrows(UserNotFoundException.class, () -> underTest.resetPassword(request));
        verify(userService, times(1)).getUserByEmail(eq(email));
        verify(userService, never()).resetPassword(any(User.class), eq(request.newPassword()));
    }

    @Test
    public void resetPassword_ExistingUserAndExpiredToken_TokenExpiredException() {
        // given
        var email = "any@gmail.com";
        var token = "token";
        var request = new ResetPasswordRequest(email, token, "password");
        var user = new User("", "", email, "", null);
        var resetPasswordToken = new ResetPasswordToken(token, null, LocalDateTime.now().minusMinutes(5));
        user.setResetPasswordToken(resetPasswordToken);

        when(userService.getUserByEmail(email)).thenReturn(user);

        // then
        assertThrows(TokenExpiredException.class, () -> underTest.resetPassword(request));
        verify(userService, times(1)).getUserByEmail(eq(email));
        verify(userService, never()).resetPassword(eq(user), eq(request.newPassword()));
    }

    @Test
    public void resetPassword_InvalidUserEmail_InvalidEmailFormatException() {
        // given
        var email = "invalid";
        var token = "token";
        var request = new ResetPasswordRequest(email, token, "password");

        // then
        assertThrows(InvalidEmailFormatException.class, () -> underTest.resetPassword(request));
        verify(userService, never()).getUserByEmail(eq(email));
        verify(userService, never()).resetPassword(any(User.class), eq(request.newPassword()));
    }

    @Test
    public void isResetPasswordTokenValid_ValidToken_True() {
        // given
        var token = "valid-token";

        when(userService.isResetPasswordTokenValid(token)).thenReturn(true);

        // when
        var response = underTest.isResetPasswordTokenValid(token);

        // then
        assertTrue(response);
        verify(userService, times(1)).isResetPasswordTokenValid(eq(token));
    }

    @Test
    public void isTokenValid_InvalidToken_False() {
        // given
        var token = "invalid-token";

        when(userService.isResetPasswordTokenValid(token)).thenReturn(false);

        // when
        var response = underTest.isResetPasswordTokenValid(token);

        // then
        assertFalse(response);
        verify(userService, times(1)).isResetPasswordTokenValid(eq(token));
    }
}
