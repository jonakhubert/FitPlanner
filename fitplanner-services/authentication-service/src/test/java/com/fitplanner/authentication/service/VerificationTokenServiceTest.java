package com.fitplanner.authentication.service;

import com.fitplanner.authentication.exception.model.TokenNotFoundException;
import com.fitplanner.authentication.model.confirmationtoken.ConfirmationToken;
import com.fitplanner.authentication.repository.ConfirmationTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConfirmationTokenServiceTest {

    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;

    @InjectMocks
    private ConfirmationTokenService underTest;

    @Test
    public void saveToken_ConfirmationToken_Saved() {
        // given
        ConfirmationToken confirmationToken = new ConfirmationToken();

        // when
        underTest.saveToken(confirmationToken);

        // then
        verify(confirmationTokenRepository, times(1)).save(eq(confirmationToken));
    }

    @Test
    public void getToken_ExistingToken_ConfirmationToken() {
        // given
        String token = "token";
        ConfirmationToken confirmationToken = new ConfirmationToken();

        when(confirmationTokenRepository.findByToken(anyString())).thenReturn(Optional.of(confirmationToken));

        // when
        ConfirmationToken result = underTest.getToken(token);

        // then
        assertEquals(result, confirmationToken);
        verify(confirmationTokenRepository, times(1)).findByToken(eq(token));
    }

    @Test
    public void getToken_NonExistingToken_TokenNotFoundException() {
        // given
        String token = "non-existing";

        when(confirmationTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // then
        assertThrows(TokenNotFoundException.class, () -> underTest.getToken(token));
        verify(confirmationTokenRepository, times(1)).findByToken(token);
    }

    @Test
    public void setConfirmedAt_ExistingToken_Updated() {
        // given
        ConfirmationToken confirmationToken = new ConfirmationToken();

        when(confirmationTokenRepository.findByToken(anyString())).thenReturn(Optional.of(confirmationToken));

        // when
        underTest.setConfirmedAt(anyString());

        // then
        assertNotNull(confirmationToken.getConfirmedAt());
        verify(confirmationTokenRepository, times(1)).findByToken(anyString());
        verify(confirmationTokenRepository, times(1)).save(any(ConfirmationToken.class));
    }

    @Test
    public void setConfirmedAt_NonExistingToken_TokenNotFoundException() {
        // given
        String token = "non-existing";

        when(confirmationTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // then
        assertThrows(TokenNotFoundException.class, () -> underTest.setConfirmedAt(token));
        verify(confirmationTokenRepository, times(1)).findByToken(anyString());
        verify(confirmationTokenRepository, never()).save(any(ConfirmationToken.class));
    }

    @Test
    public void deleteToken_ExistingUserEmail_Removal() {
        // given
        String email = "any@gmail.com";

        when(confirmationTokenRepository.findByUserEmail(email)).thenReturn(Optional.of(new ConfirmationToken()));

        // when
        underTest.deleteToken(email);

        // then
        verify(confirmationTokenRepository, times(1)).delete(any(ConfirmationToken.class));
        verify(confirmationTokenRepository, times(1)).findByUserEmail(eq(email));
    }
}
