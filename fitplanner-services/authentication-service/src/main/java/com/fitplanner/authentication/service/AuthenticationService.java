package com.fitplanner.authentication.service;

import com.fitplanner.authentication.exception.model.*;
import com.fitplanner.authentication.model.api.*;
import com.fitplanner.authentication.util.EmailBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(
        UserService userService,
        EmailService emailService,
        JwtService jwtService,
        AuthenticationManager authenticationManager
    ) {
        this.userService = userService;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public ConfirmationResponse register(RegisterRequest request) {
        if(!isEmailValid(request.email()))
            throw new InvalidEmailFormatException(request.email() + " format is invalid.");

        if(userService.isUserExist(request.email()))
            throw new UserAlreadyExistException(request.email() + " already exist.");

        var user = userService.createUser(request);
        var verificationToken = userService.createVerificationToken(user);

        var link = "http://localhost:8222/api/auth/verify?verification_token=" + verificationToken.getToken();
        emailService.send(user.getUsername(), "Confirm your account", EmailBuilder.buildEmailBody(link, "Confirm"));

        return new ConfirmationResponse("Verification email has been sent.");
    }

    public LoginResponse login(LoginRequest request) {
        if(!isEmailValid(request.email()))
            throw new InvalidEmailFormatException(request.email() + " format is invalid.");

        var user = userService.getUserByEmail(request.email());

        if(!user.isEnabled()) {
            var verificationToken = userService.createVerificationToken(user);

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
        var accessToken = userService.createAccessToken(user, jwt);

        return new LoginResponse(accessToken.getToken());
    }

    public boolean isAccessTokenValid(String token) {
        return userService.isAccessTokenValid(token);
    }

    public ConfirmationResponse verify(String token) {
        var verificationToken = userService.getVerificationToken(token);

        if(verificationToken == null)
            throw new TokenNotFoundException("Token not found.");

        if(verificationToken.getConfirmedAt() != null)
            throw new UserAlreadyVerifiedException("User has been already verified.");

        if(verificationToken.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new TokenExpiredException("Token is expired.");

        var user = userService.getUserByVerificationToken(token);
        userService.verifyUser(user);

        return new ConfirmationResponse("User account verified.");
    }

    public ConfirmationResponse forgotPassword(String email) {
        if(!isEmailValid(email))
            throw new InvalidEmailFormatException(email + " format is invalid.");

        var user = userService.getUserByEmail(email);

        var resetPasswordToken = userService.createResetPasswordToken(user);
        var link = "http://localhost:4200/reset-password?email=" + email + "&token=" + resetPasswordToken.getToken();

        emailService.send(user.getUsername(), "Reset password", EmailBuilder.buildEmailBody(link, "Reset password"));

        return new ConfirmationResponse("Reset password has been sent.");
    }

    public ConfirmationResponse resetPassword(ResetPasswordRequest request) {
        if(!isEmailValid(request.email()))
            throw new InvalidEmailFormatException(request.email() + " format is invalid.");

        var user = userService.getUserByEmail(request.email());

        if(user.getResetPasswordToken().getExpiredAt().isBefore(LocalDateTime.now()))
            throw new TokenExpiredException("Token is expired.");

        if(!user.getResetPasswordToken().getToken().equals(request.resetPasswordToken()))
            throw new TokenExpiredException("Token is expired.");

        userService.resetPassword(user, request.newPassword());

        return new ConfirmationResponse("Password reset successfully.");
    }

    public boolean isResetPasswordTokenValid(String token) {
        return userService.isResetPasswordTokenValid(token);
    }

    private boolean isEmailValid(String email) {
        var regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(regexPattern)
            .matcher(email)
            .matches();
    }
}
