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
@RequestMapping(path = "/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(
        path = "/register",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConfirmationResponse> register(
        @Valid @RequestBody RegisterRequest registerRequest
    ) {
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @PostMapping(
        path = "/login",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<LoginResponse> login(
        @Valid @RequestBody LoginRequest loginRequest
    ) {
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

    @PostMapping(
        path = "/validate-access-token"
    )
    public ResponseEntity<Void> validateToken(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            var token = authorizationHeader.substring(7);

            if(authenticationService.isTokenValid(token))
                return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping(
        path = "/verify",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConfirmationResponse> verify(
        @RequestParam("verification_token") String verificationToken
    ) {
        return ResponseEntity.ok(authenticationService.verify(verificationToken));
    }

    @PostMapping(
        path = "/forgot-password",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConfirmationResponse> forgotPassword(
        @RequestParam("email") String email
    ) {
        return ResponseEntity.ok(authenticationService.forgotPassword(email));
    }

    @PostMapping(
        path = "/reset-password",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConfirmationResponse> resetPassword(
        @RequestBody @Valid ResetPasswordRequest resetPasswordRequest
    ) {
        return ResponseEntity.ok(authenticationService.resetPassword(resetPasswordRequest));
    }
}
