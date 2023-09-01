package com.fitplanner.authentication.model.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @NotNull @NotEmpty String firstName,
        @NotNull @NotEmpty String lastName,
        @NotNull @NotEmpty String email,
        @NotNull @NotEmpty String password
) {}
