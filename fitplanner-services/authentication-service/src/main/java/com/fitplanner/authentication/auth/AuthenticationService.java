package com.fitplanner.authentication.auth;

import com.fitplanner.authentication.exception.UserAlreadyExistException;
import com.fitplanner.authentication.jwt.JwtService;
import com.fitplanner.authentication.token.ITokenRepository;
import com.fitplanner.authentication.token.Token;
import com.fitplanner.authentication.user.Role;
import com.fitplanner.authentication.user.User;
import com.fitplanner.authentication.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final ITokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(
        UserRepository userRepository,
        ITokenRepository tokenRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        if(userRepository.findByEmail(registerRequest.email()).isPresent())
            throw new UserAlreadyExistException(registerRequest.email() + " already exist.");

        User user = new User(
            registerRequest.firstName(),
            registerRequest.lastName(),
            registerRequest.email(),
            passwordEncoder.encode(registerRequest.password()),
            Role.USER
        );

        User savedUser = userRepository.save(user);
        String jwt = jwtService.generateToken(user);

        saveUserToken(jwt, savedUser);

        return new AuthenticationResponse(jwt);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authenticationRequest.email(),
                authenticationRequest.password()
            )
        );

        User user = userRepository.findByEmail(authenticationRequest.email())
            .orElseThrow(() -> new UsernameNotFoundException(authenticationRequest.email() + " not found."));

        String jwt = jwtService.generateToken(user);

        revokeAllUserTokens(user);
        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt);
    }

    private void saveUserToken(String jwt, User user) {
        Token token = new Token(jwt, false, false, user.getUsername());
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokensByUser(user.getUsername());

        if(validUserTokens.isEmpty())
            return;

        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }
}
