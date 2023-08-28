package com.fitplanner.authentication.model.api;

import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull String email,
        @NotNull String password
) {}
