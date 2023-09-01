package com.fitplanner.authentication.service;

import com.fitplanner.authentication.exception.model.InvalidEmailFormatException;
import com.fitplanner.authentication.exception.model.UserAlreadyExistException;
import com.fitplanner.authentication.exception.model.UserNotFoundException;
import com.fitplanner.authentication.model.api.AuthenticationRequest;
import com.fitplanner.authentication.model.api.AuthenticationResponse;
import com.fitplanner.authentication.model.api.RegisterRequest;
import com.fitplanner.authentication.model.token.Token;
import com.fitplanner.authentication.model.user.User;
import com.fitplanner.authentication.repository.TokenRepository;
import com.fitplanner.authentication.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService underTest;

    @Test
    public void shouldReturnAuthenticationResponseWithAccessTokenWhenValidRegisterRequestIsProvided() {
        // given
        RegisterRequest registerRequest = new RegisterRequest("any", "any", "valid@gmail.com", "any");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User());
        when(jwtService.generateToken(any())).thenReturn("token");

        // when
        AuthenticationResponse response = underTest.register(registerRequest);

        // then
        assertNotNull(response);
        assertEquals("token", response.accessToken());
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    public void shouldThrowInvalidEmailFormatExceptionWhenRegisterRequestWithInvalidEmailIsProvided() {
        // given
        RegisterRequest registerRequest = new RegisterRequest("any", "any", "invalid", "any");

        // when & then
        assertThrows(InvalidEmailFormatException.class, () -> underTest.register(registerRequest));
        verify(userRepository, never()).save(any());
        verify(tokenRepository, never()).save(any(Token.class));
        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    public void shouldThrowUserAlreadyExistExceptionWhenRegisterRequestWithExistingEmailIsProvided() {
        // given
        RegisterRequest registerRequest = new RegisterRequest("any", "any", "existing@gmail.com", "any");

        when(userRepository.findByEmail(registerRequest.email())).thenReturn(Optional.of(new User()));

        // when & then
        assertThrows(UserAlreadyExistException.class, () -> underTest.register(registerRequest));
        verify(userRepository, never()).save(any());
        verify(tokenRepository, never()).save(any(Token.class));
        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    public void shouldReturnAuthenticationResponseWithAccessTokenWhenValidAuthenticationRequestIsProvided() {
        // given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("any@email.com", "any");

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail(authenticationRequest.email())).thenReturn(Optional.of(new User()));
        when(jwtService.generateToken(any())).thenReturn("token");
        when(tokenRepository.findByUserEmail(any())).thenReturn(Optional.of(new Token()));

        // when
        AuthenticationResponse response = underTest.authenticate(authenticationRequest);

        // then
        assertNotNull(response);
        assertEquals("token", response.accessToken());
        verify(tokenRepository, times(1)).save(any(Token.class));
        verify(tokenRepository, times(1)).delete(any(Token.class));
    }

    @Test
    public void shouldThrowInvalidEmailFormatExceptionWhenAuthenticationRequestWithInvalidEmailIsProvided() {
        // given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("invalid", "any");

        // when & then
        assertThrows(InvalidEmailFormatException.class, () -> underTest.authenticate(authenticationRequest));
        verify(authenticationManager, never()).authenticate(any());
        verify(userRepository, never()).findByEmail(anyString());
        verify(jwtService, never()).generateToken(any(User.class));
        verify(tokenRepository, never()).save((any(Token.class)));
        verify(tokenRepository, never()).findByUserEmail(anyString());
        verify(tokenRepository, never()).delete(any(Token.class));
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenAuthenticationRequestWithNonExistingEmailIsProvided() {
        // given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("any@gmail.com", "any");

        // when & then
        assertThrows(UserNotFoundException.class, () -> underTest.authenticate(authenticationRequest));
        verify(authenticationManager, never()).authenticate(any());
        verify(jwtService, never()).generateToken(any(User.class));
        verify(tokenRepository, never()).save((any(Token.class)));
        verify(tokenRepository, never()).findByUserEmail(anyString());
        verify(tokenRepository, never()).delete(any(Token.class));
    }
}
