package com.fitplanner.authentication.model.api;

import jakarta.validation.constraints.NotNull;

public record AuthenticationRequest(
        @NotNull String email,
        @NotNull String password
) {}
