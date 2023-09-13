package com.fitplanner.authentication.service;

import com.fitplanner.authentication.exception.model.*;
import com.fitplanner.authentication.model.api.LoginRequest;
import com.fitplanner.authentication.model.api.LoginResponse;
import com.fitplanner.authentication.model.api.RegisterRequest;
import com.fitplanner.authentication.model.api.ConfirmationResponse;
import com.fitplanner.authentication.model.confirmationtoken.ConfirmationToken;
import com.fitplanner.authentication.model.accesstoken.AccessToken;
import com.fitplanner.authentication.model.user.Role;
import com.fitplanner.authentication.model.user.User;
import com.fitplanner.authentication.repository.UserRepository;
import com.fitplanner.authentication.util.EmailBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AccessTokenService accessTokenService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(
        UserRepository userRepository,
        AccessTokenService accessTokenService,
        ConfirmationTokenService confirmationTokenService,
        EmailService emailService,
        JwtService jwtService,
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.accessTokenService = accessTokenService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public ConfirmationResponse register(RegisterRequest registerRequest) {
        String token = UUID.randomUUID().toString();
        String link = "http://localhost:8222/api/auth/verify?confirmation_token=" + token;

        if(!isEmailValid(registerRequest.email()))
            throw new InvalidEmailFormatException(registerRequest.email() + " format is invalid.");

        Optional<User> newUser = userRepository.findByEmail(registerRequest.email());

        if(newUser.isPresent() && newUser.get().isEnabled())
            throw new UserAlreadyExistException(registerRequest.email() + " already exist.");

        if(newUser.isPresent() && !newUser.get().isEnabled()) {
            emailService.send(
                registerRequest.email(),
                EmailBuilder.buildEmail(registerRequest.firstName(), link)
            );

            confirmationTokenService.deleteToken(registerRequest.email());
            saveConfirmationToken(token, registerRequest.email());

            return new ConfirmationResponse("Verification email has been resent.");
        }

        emailService.send(
            registerRequest.email(),
                EmailBuilder.buildEmail(registerRequest.firstName(), link)
        );

        User user = new User(
            registerRequest.firstName(),
            registerRequest.lastName(),
            registerRequest.email(),
            passwordEncoder.encode(registerRequest.password()),
            Role.USER
        );

        User savedUser = userRepository.save(user);
        saveConfirmationToken(token, savedUser.getUsername());

        return new ConfirmationResponse("Verification email has been sent.");
    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        if(!isEmailValid(loginRequest.email()))
            throw new InvalidEmailFormatException(loginRequest.email() + " format is invalid.");

        User user = userRepository.findByEmail(loginRequest.email())
            .orElseThrow(() -> new UserNotFoundException("User not found."));

        if(!user.isEnabled())
            throw new UserNotVerifiedException("User is not verified.");

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password()
            )
        );

        String jwt = jwtService.generateToken(user);
        AccessToken accessToken = new AccessToken(jwt, user.getUsername());

        accessTokenService.deleteToken(user);
        accessTokenService.saveToken(accessToken);

        return new LoginResponse(jwt);
    }

    @Transactional
    public ConfirmationResponse verify(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);

        if(confirmationToken.getConfirmedAt() != null)
            throw new UserAlreadyVerifiedException("User has been already verified.");

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if(expiredAt.isBefore(LocalDateTime.now()))
            throw new TokenExpiredException("Token is expired.");

        confirmationTokenService.setConfirmedAt(token);
        verifyUser(confirmationToken.getUserEmail());

        return new ConfirmationResponse("User account verified.");
    }

    public boolean isTokenValid(String token) {
        return accessTokenService.isTokenValid(token);
    }

    private void verifyUser(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setEnabled(true);
        userRepository.save(user);
    }

    private void saveConfirmationToken(String token, String email) {
        ConfirmationToken confirmationToken = new ConfirmationToken(
            token,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15),
            email
        );

        confirmationTokenService.saveToken(confirmationToken);
    }

    private boolean isEmailValid(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }
}
