package com.fitplanner.authentication.controller;

import com.fitplanner.authentication.model.api.LoginRequest;
import com.fitplanner.authentication.model.api.LoginResponse;
import com.fitplanner.authentication.model.api.RegisterResponse;
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
    public ResponseEntity<RegisterResponse> register(
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

    @GetMapping(
        path = "/verify",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public RegisterResponse verify(
        @RequestParam("verification_token") String verificationToken
    ) {
        return authenticationService.verify(verificationToken);
    }


    @PostMapping(
        path = "/validate-token"
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
}
