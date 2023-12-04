package com.fitplanner.authentication.service;

import com.fitplanner.authentication.exception.model.UserNotFoundException;
import com.fitplanner.authentication.model.api.RegisterRequest;
import com.fitplanner.authentication.model.tokens.ResetPasswordToken;
import com.fitplanner.authentication.model.tokens.VerificationToken;
import com.fitplanner.authentication.model.tokens.accesstoken.AccessToken;
import com.fitplanner.authentication.model.user.Role;
import com.fitplanner.authentication.model.user.User;
import com.fitplanner.authentication.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService underTest;

    @Test
    public void getUserByEmail_ExistingEmail_User() {
        // given
        var email = "any@gmail.com";
        var user = new User();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        var result = underTest.getUserByEmail(email);

        // then
        assertEquals(result, user);
        verify(userRepository, times(1)).findByEmail(eq(email));
    }

    @Test
    public void getUserEmail_NonExistingEmail_UserNotFoundException() {
        // given
        var email = "any@gmail.com";

        when(userRepository.findByEmail(email)).thenThrow(new UserNotFoundException("User not found."));

        // then
        assertThrows(UserNotFoundException.class, () -> underTest.getUserByEmail(email));
        verify(userRepository, times(1)).findByEmail(eq(email));
    }

    @Test
    public void getUserByVerificationToken_ExistingToken_User() {
        // given
        var token = "token";
        var user = new User();

        when(userRepository.findByVerificationToken(token)).thenReturn(Optional.of(user));

        // when
        var result = underTest.getUserByVerificationToken(token);

        // then
        assertEquals(result, user);
        verify(userRepository, times(1)).findByVerificationToken(token);
    }

    @Test
    public void getUserByVerificationToken_NonExistingToken_UserNotFoundException() {
        // given
        var token = "token";

        when(userRepository.findByVerificationToken(token)).thenThrow(new UserNotFoundException("User not found."));

        // then
        assertThrows(UserNotFoundException.class, () -> underTest.getUserByVerificationToken(token));
        verify(userRepository, times(1)).findByVerificationToken(eq(token));
    }

    @Test
    public void createUser_RegisterRequest_User() {
        // given
        var encodedPassword = "encodedPassword";
        var request = new RegisterRequest("any", "any", 191.0, 88.0, 1, 2, "any@gmail.com", "anyany");
        var user = new User(request.firstName(), request.lastName(), request.email(), encodedPassword, Role.USER);

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);

        // when
        var result = underTest.createUser(request);

        // then
        assertEquals(result, user);
    }

    @Test
    public void verifyUser_User_UserEnabled() {
        // given
        var user = new User();
        var verificationToken = new VerificationToken();
        user.setVerificationToken(verificationToken);

        // when
        underTest.verifyUser(user);

        // then
        assertTrue(user.isEnabled());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void isUserExist_ExistingEmail_True() {
        // given
        var email = "any@gmail.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        // when
        var result = underTest.isUserExist(email);

        // then
        assertTrue(result);
        verify(userRepository, times(1)).findByEmail(eq(email));
    }

    @Test
    public void isUserExist_NonExistingEmail_False() {
        // given
        var email = "any@gmail.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when
        var result = underTest.isUserExist(email);

        // then
        assertFalse(result);
        verify(userRepository, times(1)).findByEmail(eq(email));
    }

    @Test
    public void resetPassword_UserAndNewPassword_PasswordChanged() {
        // given
        var newPassword = "password";
        var encodedPassword = "encodedPassword";
        var user = new User();

        user.setAccessToken(new AccessToken("access-token"));
        user.setResetPasswordToken(new ResetPasswordToken("reset-password-token", null, null));

        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);

        // when
        underTest.resetPassword(user, newPassword);

        // then
        assertNull(user.getAccessToken());
        assertNull(user.getResetPasswordToken());
        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void getAccessToken_ExistingToken_AccessToken() {
        // given
        var token = "validToken";
        var accessToken = new AccessToken(token);
        var user = new User();
        user.setAccessToken(accessToken);

        when(userRepository.findByAccessToken(token)).thenReturn(Optional.of(user));

        // when
        var result = underTest.getAccessToken(token);

        // then
        assertEquals(result, accessToken);
        verify(userRepository, times(1)).findByAccessToken(eq(token));
    }

    @Test
    public void getAccessToken_NonExistingToken_Null() {
        // given
        var token = "validToken";

        when(userRepository.findByAccessToken(token)).thenReturn(Optional.empty());

        // when
        var result = underTest.getAccessToken(token);

        // then
        assertNull(result);
        verify(userRepository, times(1)).findByAccessToken(eq(token));
    }

    @Test
    public void createAccessToken_UserAndToken_AccessToken() {
        // given
        var token = "token";
        var accessToken = new AccessToken(token);
        var user = new User();

        // when
        var result = underTest.createAccessToken(user,token);

        // then
        assertEquals(result, accessToken);
        assertEquals(user.getAccessToken(), accessToken);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void revokeAccessToken_ExistingToken_TokenRevoked() {
        // given
        var token = "token";
        var accessToken = new AccessToken(token);
        var user = new User();
        user.setAccessToken(accessToken);

        when(userRepository.findByAccessToken(token)).thenReturn(Optional.of(user));

        // when
        underTest.revokeAccessToken(token);

        // then
        assertNull(user.getAccessToken());
        verify(userRepository, times(1)).findByAccessToken(eq(token));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void revokeAccessToken_NonExistingToken_UserNotFoundException() {
        // given
        var token = "token";

        when(userRepository.findByAccessToken(token)).thenThrow(new UserNotFoundException("User not found."));

        // then
        assertThrows(UserNotFoundException.class, () -> underTest.revokeAccessToken(token));
        verify(userRepository, times(1)).findByAccessToken(eq(token));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void isAccessTokenValid_ValidToken_True() {
        // given
        var token = "token";

        when(userRepository.findByAccessToken(token)).thenReturn(Optional.of(new User()));

        // when
        var result = underTest.isAccessTokenValid(token);

        // then
        assertTrue(result);
        verify(userRepository, times(1)).findByAccessToken(eq(token));
    }

    @Test
    public void isAccessTokenValid_InvalidToken_False() {
        // given
        var token = "token";

        when(userRepository.findByAccessToken(token)).thenReturn(Optional.empty());

        // when
        var result = underTest.isAccessTokenValid(token);

        // then
        assertFalse(result);
        verify(userRepository, times(1)).findByAccessToken(eq(token));
    }

    @Test
    public void createVerificationToken_User_VerificationToken() {
        // given
        var user = new User();

        // when
        var result = underTest.createVerificationToken(user);

        // then
        assertEquals(result, user.getVerificationToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void getVerificationToken_ExistingToken_VerificationToken() {
        // given
        var token = "validToken";
        var verificationToken = new VerificationToken(token, null, null);
        var user = new User();
        user.setVerificationToken(verificationToken);

        when(userRepository.findByVerificationToken(token)).thenReturn(Optional.of(user));

        // when
        var result = underTest.getVerificationToken(token);

        // then
        assertEquals(result, verificationToken);
        verify(userRepository, times(1)).findByVerificationToken(eq(token));
    }

    @Test
    public void getVerificationToken_NonExistingToken_Null() {
        // given
        var token = "validToken";

        when(userRepository.findByVerificationToken(token)).thenReturn(Optional.empty());

        // when
        var result = underTest.getVerificationToken(token);

        // then
        assertNull(result);
        verify(userRepository, times(1)).findByVerificationToken(eq(token));
    }

    @Test
    public void createResetPasswordToken_Token_ResetPasswordToken() {
        // given
        var user = new User();

        // when
        var result = underTest.createResetPasswordToken(user);

        // then
        assertEquals(result, user.getResetPasswordToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void isResetPasswordTokenValid_ValidToken_True() {
        // given
        var token = "token";

        when(userRepository.findByResetPasswordToken(token)).thenReturn(Optional.of(new User()));

        // when
        var result = underTest.isResetPasswordTokenValid(token);

        // then
        assertTrue(result);
        verify(userRepository, times(1)).findByResetPasswordToken(eq(token));
    }

    @Test
    public void isResetPasswordTokenValid_InvalidToken_False() {
        // given
        var token = "token";

        when(userRepository.findByResetPasswordToken(token)).thenReturn(Optional.empty());

        // when
        var result = underTest.isResetPasswordTokenValid(token);

        // then
        assertFalse(result);
        verify(userRepository, times(1)).findByResetPasswordToken(eq(token));
    }
}
