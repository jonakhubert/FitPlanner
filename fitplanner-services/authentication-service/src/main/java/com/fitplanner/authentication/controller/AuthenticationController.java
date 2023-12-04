package com.fitplanner.authentication.controller;

import com.fitplanner.authentication.model.api.*;
import com.fitplanner.authentication.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConfirmationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping(path = "/access-tokens")
    public ResponseEntity<Void> validateAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            var token = authorizationHeader.substring(7);

            if(authenticationService.isAccessTokenValid(token))
                return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping(path = "/verification-tokens/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConfirmationResponse> verify(@PathVariable("token") String verificationToken) {
        return ResponseEntity.ok(authenticationService.verify(verificationToken));
    }

    @PostMapping(path = "/users/{email}/password-reminder", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConfirmationResponse> forgotPassword(@PathVariable("email") String email) {
        return ResponseEntity.ok(authenticationService.forgotPassword(email));
    }

    @PostMapping(path = "/users/{email}/password-reset", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConfirmationResponse> resetPassword(@PathVariable("email") String email,
        @RequestBody @Valid ResetPasswordRequest request
    ) {
        return ResponseEntity.ok(authenticationService.resetPassword(email, request));
    }

    @PostMapping(path = "/reset-password-tokens", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> validateResetPasswordToken(@RequestHeader("X-Reset-Password-Token") String token) {
        if(token != null && authenticationService.isResetPasswordTokenValid(token))
            return ResponseEntity.ok().build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
