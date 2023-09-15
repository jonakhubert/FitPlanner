package com.fitplanner.authentication.service;

import com.fitplanner.authentication.exception.model.TokenNotFoundException;
import com.fitplanner.authentication.model.verificationtoken.VerificationToken;
import com.fitplanner.authentication.repository.VerificationTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VerificationTokenServiceTest {

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @InjectMocks
    private VerificationTokenService underTest;

    @Test
    public void saveToken_ConfirmationToken_Saved() {
        // given
        var verificationToken = new VerificationToken();

        // when
        underTest.saveToken(verificationToken);

        // then
        verify(verificationTokenRepository, times(1)).save(eq(verificationToken));
    }

    @Test
    public void getToken_ExistingToken_ConfirmationToken() {
        // given
        var token = "token";
        var verificationToken = new VerificationToken();

        when(verificationTokenRepository.findByToken(anyString())).thenReturn(Optional.of(verificationToken));

        // when
        var result = underTest.getToken(token);

        // then
        assertEquals(result, verificationToken);
        verify(verificationTokenRepository, times(1)).findByToken(eq(token));
    }

    @Test
    public void getToken_NonExistingToken_TokenNotFoundException() {
        // given
        var token = "non-existing";

        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // then
        assertThrows(TokenNotFoundException.class, () -> underTest.getToken(token));
        verify(verificationTokenRepository, times(1)).findByToken(token);
    }

    @Test
    public void setConfirmedAt_ExistingToken_Updated() {
        // given
        var verificationToken = new VerificationToken();

        when(verificationTokenRepository.findByToken(anyString())).thenReturn(Optional.of(verificationToken));

        // when
        underTest.setConfirmedAt(anyString());

        // then
        assertNotNull(verificationToken.getConfirmedAt());
        verify(verificationTokenRepository, times(1)).findByToken(anyString());
        verify(verificationTokenRepository, times(1)).save(any(VerificationToken.class));
    }

    @Test
    public void setConfirmedAt_NonExistingToken_TokenNotFoundException() {
        // given
        var token = "non-existing";

        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // then
        assertThrows(TokenNotFoundException.class, () -> underTest.setConfirmedAt(token));
        verify(verificationTokenRepository, times(1)).findByToken(anyString());
        verify(verificationTokenRepository, never()).save(any(VerificationToken.class));
    }

    @Test
    public void deleteToken_ExistingUserEmail_Removal() {
        // given
        var email = "any@gmail.com";

        when(verificationTokenRepository.findByUserEmail(email)).thenReturn(Optional.of(new VerificationToken()));

        // when
        underTest.deleteToken(email);

        // then
        verify(verificationTokenRepository, times(1)).delete(any(VerificationToken.class));
        verify(verificationTokenRepository, times(1)).findByUserEmail(eq(email));
    }
}
