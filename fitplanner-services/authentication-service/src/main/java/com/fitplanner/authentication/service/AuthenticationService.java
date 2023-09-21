package com.fitplanner.authentication.service;

import com.fitplanner.authentication.exception.model.*;
import com.fitplanner.authentication.model.api.*;
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
import java.util.regex.Pattern;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(
        UserRepository userRepository,
        TokenService tokenService,
        EmailService emailService,
        JwtService jwtService,
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public ConfirmationResponse register(RegisterRequest request) {
        if(!isEmailValid(request.email()))
            throw new InvalidEmailFormatException(request.email() + " format is invalid.");

        if(userRepository.findByEmail(request.email()).isPresent())
            throw new UserAlreadyExistException(request.email() + " already exist.");

        var user = createUser(request);
        var verificationToken = tokenService.createVerificationToken(user.getUsername());
        var link = "http://localhost:8222/api/auth/verify?verification_token=" + verificationToken.getToken();

        emailService.send(user.getUsername(), "Confirm your account", EmailBuilder.buildEmailBody(link, "Confirm"));

        return new ConfirmationResponse("Verification email has been sent.");
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        if(!isEmailValid(request.email()))
            throw new InvalidEmailFormatException(request.email() + " format is invalid.");

        var user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new UserNotFoundException("User not found."));

        if(!user.isEnabled()) {
            tokenService.deleteVerificationToken(request.email());
            var verificationToken = tokenService.createVerificationToken(user.getUsername());
            var link = "http://localhost:8222/api/auth/verify?verification_token=" + verificationToken.getToken();

            emailService.send(user.getUsername(), "Confirm your account", EmailBuilder.buildEmailBody(link, "Confirm"));

            throw new UserNotVerifiedException("User is not verified. Verification email has been resent.");
        }

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
            )
        );

        var jwt = jwtService.generateToken(user);
        tokenService.deleteAccessToken(user.getUsername());
        tokenService.createAccessToken(jwt, user.getUsername());

        return new LoginResponse(jwt);
    }

    public boolean isAccessTokenValid(String token) {
        return tokenService.isAccessTokenValid(token);
    }

    @Transactional
    public ConfirmationResponse verify(String token) {
        var verificationToken = tokenService.findVerificationToken(token);

        if(verificationToken.getConfirmedAt() != null)
            throw new UserAlreadyVerifiedException("User has been already verified.");

        if(verificationToken.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new TokenExpiredException("Token is expired.");

        verificationToken.setConfirmedAt(LocalDateTime.now());
        tokenService.saveVerificationToken(verificationToken);

        verifyUser(verificationToken.getUserEmail());

        return new ConfirmationResponse("User account verified.");
    }

    @Transactional
    public ConfirmationResponse forgotPassword(String email) {
        if(!isEmailValid(email))
            throw new InvalidEmailFormatException(email + " format is invalid.");

        var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found."));

        tokenService.deleteResetPasswordToken(email);
        var resetPasswordToken = tokenService.createResetPasswordToken(email);
        var link = "http://localhost:4200/reset-password?email=" + email + "&token=" + resetPasswordToken.getToken();

        emailService.send(user.getUsername(), "Reset password", EmailBuilder.buildEmailBody(link, "Reset password"));

        return new ConfirmationResponse("Reset password has been sent.");
    }

    @Transactional
    public ConfirmationResponse resetPassword(ResetPasswordRequest request) {
        if(!isEmailValid(request.email()))
            throw new InvalidEmailFormatException(request.email() + " format is invalid.");

        var user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new UserNotFoundException("User not found."));

        var resetPasswordToken = tokenService.findResetPasswordToken(request.resetPasswordToken());
        var expiredAt = resetPasswordToken.getExpiredAt();

        if(expiredAt.isBefore(LocalDateTime.now()))
            throw new TokenExpiredException("Token is expired.");

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        tokenService.deleteResetPasswordToken(user.getUsername());
        tokenService.deleteAccessToken(user.getUsername());

        return new ConfirmationResponse("Password reset successfully.");
    }

    public boolean isResetPasswordTokenValid(String token) {
        return tokenService.isResetPasswordTokenValid(token);
    }

    private void verifyUser(String email) {
        var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setEnabled(true);
        userRepository.save(user);
    }

    private User createUser(RegisterRequest request) {
        var user = new User(
            request.firstName(),
            request.lastName(),
            request.email(),
            passwordEncoder.encode(request.password()),
            Role.USER
        );

        return userRepository.save(user);
    }

    private boolean isEmailValid(String email) {
        var regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }
}
