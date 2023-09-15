package com.fitplanner.authentication.service;

import com.fitplanner.authentication.model.accesstoken.AccessToken;
import com.fitplanner.authentication.model.user.Role;
import com.fitplanner.authentication.model.user.User;
import com.fitplanner.authentication.repository.AccessTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccessTokenServiceTest {

    @Mock
    private AccessTokenRepository accessTokenRepository;

    @InjectMocks
    private AccessTokenService underTest;

    @Test
    public void isTokenValid_ValidToken_True() {
        // given
        var token = "valid-token";

        when(accessTokenRepository.findByToken(token)).thenReturn(Optional.of(new AccessToken()));

        // when
        var result = underTest.isTokenValid(token);

        // then
        assertTrue(result);
    }

    @Test
    public void isTokenValid_InvalidToken_False() {
        // given
        var token = "invalid-token";

        when(accessTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // when
        var result = underTest.isTokenValid(token);

        // then
        assertFalse(result);
    }

    @Test
    public void saveToken_AccessToken_Saved() {
        // given
        var accessToken = new AccessToken();

        // when
        underTest.saveToken(accessToken);

        // then
        verify(accessTokenRepository, times(1)).save(accessToken);
    }

    @Test
    public void deleteToken_ExistingUser_Removal() {
        // given
        var user = new User("any", "any", "any@gmail.com", "any", Role.USER);

        when(accessTokenRepository.findByUserEmail(anyString())).thenReturn(Optional.of(new AccessToken()));

        // when
        underTest.deleteToken(user);

        // then
        verify(accessTokenRepository, times(1)).delete(any(AccessToken.class));
    }

    @Test
    public void deleteToken_NonExistingUser_NoRemoval() {
        // given
        var user = new User("any", "any", "any@gmail.com", "any", Role.USER);

        when(accessTokenRepository.findByUserEmail(anyString())).thenReturn(Optional.empty());

        // when
        underTest.deleteToken(user);

        // then
        verify(accessTokenRepository, never()).delete(any(AccessToken.class));
    }
}
