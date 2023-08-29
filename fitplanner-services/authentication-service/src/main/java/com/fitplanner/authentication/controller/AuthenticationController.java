package com.fitplanner.authentication.controller;

import com.fitplanner.authentication.model.api.AuthenticationRequest;
import com.fitplanner.authentication.model.api.AuthenticationResponse;
import com.fitplanner.authentication.service.AuthenticationService;
import com.fitplanner.authentication.model.api.RegisterRequest;
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
    public ResponseEntity<AuthenticationResponse> register(
        @Valid @RequestBody RegisterRequest registerRequest
    ) {
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @PostMapping(
        path = "/authenticate",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AuthenticationResponse> authenticate(
        @Valid @RequestBody AuthenticationRequest authenticationRequest
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Void> validateToken(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if (authenticationService.isTokenValid(token))
                return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
