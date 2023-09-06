package com.fitplanner.authentication.model.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull @NotEmpty String email,
        @NotNull @NotEmpty String password
) {}
