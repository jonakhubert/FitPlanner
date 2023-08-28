package com.fitplanner.authentication.service;

import com.fitplanner.authentication.exception.model.UserAlreadyExistException;
import com.fitplanner.authentication.model.api.AuthenticationRequest;
import com.fitplanner.authentication.model.api.AuthenticationResponse;
import com.fitplanner.authentication.model.api.RegisterRequest;
import com.fitplanner.authentication.repository.TokenRepository;
import com.fitplanner.authentication.model.token.Token;
import com.fitplanner.authentication.model.user.Role;
import com.fitplanner.authentication.model.user.User;
import com.fitplanner.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(
        UserRepository userRepository,
        TokenRepository tokenRepository,
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
            .orElseThrow();

        String jwt = jwtService.generateToken(user);

        deleteUserToken(user);
        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt);
    }

    private void saveUserToken(String jwt, User user) {
        Token token = new Token(jwt, user.getUsername());
        tokenRepository.save(token);
    }

    private void deleteUserToken(User user) {
        tokenRepository.findByUserEmail(user.getUsername()).ifPresent(tokenRepository::delete);
    }
}
