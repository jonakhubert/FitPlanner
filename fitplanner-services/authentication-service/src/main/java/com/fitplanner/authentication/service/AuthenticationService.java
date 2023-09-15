package com.fitplanner.authentication.service;

import com.fitplanner.authentication.exception.model.*;
import com.fitplanner.authentication.model.api.LoginRequest;
import com.fitplanner.authentication.model.api.LoginResponse;
import com.fitplanner.authentication.model.api.RegisterRequest;
import com.fitplanner.authentication.model.api.RegisterResponse;
import com.fitplanner.authentication.model.verificationtoken.VerificationToken;
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
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AccessTokenService accessTokenService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(
        UserRepository userRepository,
        AccessTokenService accessTokenService,
        VerificationTokenService verificationTokenService,
        EmailService emailService,
        JwtService jwtService,
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.accessTokenService = accessTokenService;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {
        var token = UUID.randomUUID().toString();
        var link = "http://localhost:8222/api/auth/verify?verification_token=" + token;

        if(!isEmailValid(registerRequest.email()))
            throw new InvalidEmailFormatException(registerRequest.email() + " format is invalid.");

        if(userRepository.findByEmail(registerRequest.email()).isPresent())
            throw new UserAlreadyExistException(registerRequest.email() + " already exist.");

        var savedUser = saveUser(registerRequest);
        saveVerificationToken(token, savedUser.getUsername());

        emailService.send(
            registerRequest.email(),
            EmailBuilder.buildEmail(registerRequest.firstName(), link)
        );

        return new RegisterResponse("Verification email has been sent.");
    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        if(!isEmailValid(loginRequest.email()))
            throw new InvalidEmailFormatException(loginRequest.email() + " format is invalid.");

        var user = userRepository.findByEmail(loginRequest.email())
            .orElseThrow(() -> new UserNotFoundException("User not found."));

        if(!user.isEnabled()) {
            var token = UUID.randomUUID().toString();
            var link = "http://localhost:8222/api/auth/verify?verification_token=" + token;

            emailService.send(
                user.getUsername(),
                EmailBuilder.buildEmail(user.getFirstName(), link)
            );

            verificationTokenService.deleteToken(user.getUsername());
            saveVerificationToken(token, user.getUsername());

            throw new UserNotVerifiedException("User is not verified. Verification email has been resent.");
        }

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password()
            )
        );

        var jwt = jwtService.generateToken(user);
        var accessToken = new AccessToken(jwt, user.getUsername());

        accessTokenService.deleteToken(user);
        accessTokenService.saveToken(accessToken);

        return new LoginResponse(jwt);
    }

    @Transactional
    public RegisterResponse verify(String token) {
        var verificationToken = verificationTokenService.getToken(token);

        if(verificationToken.getConfirmedAt() != null)
            throw new UserAlreadyVerifiedException("User has been already verified.");

        var expiredAt = verificationToken.getExpiredAt();

        if(expiredAt.isBefore(LocalDateTime.now()))
            throw new TokenExpiredException("Token is expired.");

        verificationTokenService.setConfirmedAt(token);
        verifyUser(verificationToken.getUserEmail());

        return new RegisterResponse("User account verified.");
    }

    public boolean isTokenValid(String token) {
        return accessTokenService.isTokenValid(token);
    }

    private void verifyUser(String email) {
        var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setEnabled(true);
        userRepository.save(user);
    }

    private User saveUser(RegisterRequest registerRequest) {
        var user = new User(
            registerRequest.firstName(),
            registerRequest.lastName(),
            registerRequest.email(),
            passwordEncoder.encode(registerRequest.password()),
            Role.USER
        );

        return userRepository.save(user);
    }

    private void saveVerificationToken(String token, String email) {
        var verificationToken = new VerificationToken(
            token,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15),
            email
        );

        verificationTokenService.saveToken(verificationToken);
    }

    private boolean isEmailValid(String email) {
        var regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }
}
