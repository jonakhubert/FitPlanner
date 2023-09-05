package com.fitplanner.authentication.service;

import com.fitplanner.authentication.exception.model.InvalidEmailFormatException;
import com.fitplanner.authentication.exception.model.UserAlreadyExistException;
import com.fitplanner.authentication.exception.model.UserNotFoundException;
import com.fitplanner.authentication.model.api.LoginRequest;
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

import java.util.regex.Pattern;

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
        if(!isEmailValid(registerRequest.email()))
            throw new InvalidEmailFormatException(registerRequest.email() + " format is invalid.");

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

    public AuthenticationResponse login(LoginRequest loginRequest) {
        if(!isEmailValid(loginRequest.email()))
            throw new InvalidEmailFormatException(loginRequest.email() + " format is invalid.");

        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password()
            )
        );

        String jwt = jwtService.generateToken(user);

        deleteUserToken(user);
        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt);
    }

    public boolean isTokenValid(String token) {
        return tokenRepository.findByToken(token).isPresent();
    }

    private void saveUserToken(String jwt, User user) {
        Token token = new Token(jwt, user.getUsername());
        tokenRepository.save(token);
    }

    private void deleteUserToken(User user) {
        tokenRepository.findByUserEmail(user.getUsername()).ifPresent(tokenRepository::delete);
    }

    private boolean isEmailValid(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(regexPattern)
            .matcher(email)
            .matches();
    }
}
