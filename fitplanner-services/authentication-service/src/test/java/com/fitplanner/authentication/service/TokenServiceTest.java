package com.fitplanner.authentication.service;

import com.fitplanner.authentication.exception.model.TokenNotFoundException;
import com.fitplanner.authentication.model.ResetPasswordToken;
import com.fitplanner.authentication.model.VerificationToken;
import com.fitplanner.authentication.model.accesstoken.AccessToken;
import com.fitplanner.authentication.repository.AccessTokenRepository;
import com.fitplanner.authentication.repository.ResetPasswordTokenRepository;
import com.fitplanner.authentication.repository.VerificationTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private AccessTokenRepository accessTokenRepository;
    @Mock
    private VerificationTokenRepository verificationTokenRepository;
    @Mock
    private ResetPasswordTokenRepository resetPasswordTokenRepository;

    @InjectMocks
    private TokenService underTest;

    @Test
    public void createAccessToken_UserEmailAndJwt_Save() {
        // given
        var email = "any@gmail.com";
        var jwt = "token";

        // when
        underTest.createAccessToken(email, jwt);

        // then
        verify(accessTokenRepository, times(1)).save(any(AccessToken.class));
    }

    @Test
    public void isAccessTokenValid_ValidToken_True() {
        // given
        var token = "valid-token";

        when(accessTokenRepository.findByToken(token)).thenReturn(Optional.of(new AccessToken()));

        // when
        var result = underTest.isAccessTokenValid(token);

        // then
        assertTrue(result);
        verify(accessTokenRepository, times(1)).findByToken(eq(token));
    }

    @Test
    public void isAccessTokenValid_InvalidToken_False() {
        // given
        var token = "invalid-token";

        when(accessTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // when
        var result = underTest.isAccessTokenValid(token);

        // then
        assertFalse(result);
        verify(accessTokenRepository, times(1)).findByToken(eq(token));
    }

    @Test
    public void deleteAccessToken_ExistingUserEmail_TokenRemoval() {
        // given
        var email = "any@gmail.com";
        var token = new AccessToken();

        when(accessTokenRepository.findByUserEmail(email)).thenReturn(Optional.of(token));

        // when
        underTest.deleteAccessToken(email);

        // then
        verify(accessTokenRepository, times(1)).findByUserEmail(eq(email));
        verify(accessTokenRepository, times(1)).delete(eq(token));
    }

    @Test
    public void deleteAccessToken_NonExistingUserEmail_NoTokenRemoval() {
        // given
        var email = "any@gmail.com";

        when(accessTokenRepository.findByUserEmail(email)).thenReturn(Optional.empty());

        // when
        underTest.deleteAccessToken(email);

        // then
        verify(accessTokenRepository, times(1)).findByUserEmail(eq(email));
        verify(accessTokenRepository, never()).delete(any(AccessToken.class));
    }

    @Test
    public void createVerificationToken_UserEmail_Token() {
        // given
        var email = "any@gmail.com";
        var token = new VerificationToken("token", null, null, email);

        when(verificationTokenRepository.save(any(VerificationToken.class))).thenReturn(token);

        // when
        var result = underTest.createVerificationToken(email);

        // then
        assertEquals(result, token);
        verify(verificationTokenRepository, times(1)).save(any(VerificationToken.class));
    }

    @Test
    public void saveVerificationToken_Token_Save() {
        // given
        var token = new VerificationToken();

        // when
        underTest.saveVerificationToken(token);

        // then
        verify(verificationTokenRepository, times(1)).save(eq(token));
    }

    @Test
    public void findVerificationToken_ExistingToken_Token() {
        // given
        var token = "token";
        var verificationToken = new VerificationToken();

        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));

        // when
        var result = underTest.findVerificationToken(token);

        // then
        assertEquals(result, verificationToken);
        verify(verificationTokenRepository, times(1)).findByToken(eq(token));
    }

    @Test
    public void findVerificationToken_NonExistingToken_TokenNotFoundException() {
        // given
        var token = "token";

        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // then
        assertThrows(TokenNotFoundException.class, () -> underTest.findVerificationToken(token));
        verify(verificationTokenRepository, times(1)).findByToken(eq(token));
    }

    @Test
    public void deleteVerificationToken_ExistingUserEmail_TokenRemoval() {
        // given
        var email = "any@gmail.com";
        var token = new VerificationToken();

        when(verificationTokenRepository.findByUserEmail(email)).thenReturn(Optional.of(token));

        // when
        underTest.deleteVerificationToken(email);

        // then
        verify(verificationTokenRepository, times(1)).findByUserEmail(eq(email));
        verify(verificationTokenRepository, times(1)).delete(eq(token));
    }

    @Test
    public void deleteVerificationToken_NonExistingUserEmail_NoTokenRemoval() {
        // given
        var email = "any@gmail.com";

        when(verificationTokenRepository.findByUserEmail(email)).thenReturn(Optional.empty());

        // when
        underTest.deleteVerificationToken(email);

        // then
        verify(verificationTokenRepository, times(1)).findByUserEmail(eq(email));
        verify(verificationTokenRepository, never()).delete(any(VerificationToken.class));
    }

    @Test
    public void createResetPasswordToken_UserEmail_Token() {
        // given
        var email = "any@gmail.com";
        var token = new ResetPasswordToken("token", null, null, email);

        when(resetPasswordTokenRepository.save(any(ResetPasswordToken.class))).thenReturn(token);

        // when
        var result = underTest.createResetPasswordToken(email);

        // then
        assertEquals(result, token);
        verify(resetPasswordTokenRepository, times(1)).save(any(ResetPasswordToken.class));
    }

    @Test
    public void findResetPasswordToken_ExistingToken_Token() {
        // given
        var token = "token";
        var resetPasswordToken = new ResetPasswordToken();

        when(resetPasswordTokenRepository.findByToken(token)).thenReturn(Optional.of(resetPasswordToken));

        // when
        var result = underTest.findResetPasswordToken(token);

        // then
        assertEquals(result, resetPasswordToken);
        verify(resetPasswordTokenRepository, times(1)).findByToken(eq(token));
    }

    @Test
    public void findResetPasswordToken_NonExistingToken_TokenNotFoundException() {
        // given
        var token = "token";

        when(resetPasswordTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // then
        assertThrows(TokenNotFoundException.class, () -> underTest.findResetPasswordToken(token));
        verify(resetPasswordTokenRepository, times(1)).findByToken(eq(token));
    }

    @Test
    public void isResetPasswordTokenValid_ValidToken_True() {
        // given
        var token = "token";
        var email = "any@gmail.com";
        var resetPasswordToken = new ResetPasswordToken(token, null, LocalDateTime.now().plusMinutes(15), email);

        when(resetPasswordTokenRepository.findByToken(token)).thenReturn(Optional.of(resetPasswordToken));

        // when
        var result = underTest.isResetPasswordTokenValid(token);

        // then
        assertTrue(result);
        verify(resetPasswordTokenRepository, times(1)).findByToken(eq(token));
    }

    @Test
    public void isResetPasswordTokenValid_NonExistingToken_False() {
        // given
        var token = "token";

        when(resetPasswordTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // when
        var result = underTest.isResetPasswordTokenValid(token);

        // then
        assertFalse(result);
        verify(resetPasswordTokenRepository, times(1)).findByToken(eq(token));
    }

    @Test
    public void isResetPasswordTokenValid_ExpiredToken_False() {
        // given
        var token = "token";
        var email = "any@gmail.com";
        var resetPasswordToken = new ResetPasswordToken(token, null, LocalDateTime.now().minusMinutes(15), email);

        when(resetPasswordTokenRepository.findByToken(token)).thenReturn(Optional.of(resetPasswordToken));

        // when
        var result = underTest.isResetPasswordTokenValid(token);

        // then
        assertFalse(result);
        verify(resetPasswordTokenRepository, times(1)).findByToken(eq(token));
    }

    @Test
    public void deleteResetPasswordToken_ExistingUserEmail_TokenRemoval() {
        // given
        var email = "any@gmail.com";
        var token = new ResetPasswordToken();

        when(resetPasswordTokenRepository.findByUserEmail(email)).thenReturn(Optional.of(token));

        // when
        underTest.deleteResetPasswordToken(email);

        // then
        verify(resetPasswordTokenRepository, times(1)).findByUserEmail(eq(email));
        verify(resetPasswordTokenRepository, times(1)).delete(eq(token));
    }

    @Test
    public void deleteResetPasswordToken_NonExistingUserEmail_NoTokenRemoval() {
        // given
        var email = "any@gmail.com";

        when(resetPasswordTokenRepository.findByUserEmail(email)).thenReturn(Optional.empty());

        // when
        underTest.deleteResetPasswordToken(email);

        // then
        verify(resetPasswordTokenRepository, times(1)).findByUserEmail(eq(email));
        verify(resetPasswordTokenRepository, never()).delete(any(ResetPasswordToken.class));
    }
}
