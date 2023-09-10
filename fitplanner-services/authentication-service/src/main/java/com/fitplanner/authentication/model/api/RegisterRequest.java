package com.fitplanner.authentication.model.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotNull @NotEmpty String firstName,
        @NotNull @NotEmpty String lastName,
        @NotNull @NotEmpty String email,
        @NotNull @NotEmpty @Size(min = 6) String password
) {}
