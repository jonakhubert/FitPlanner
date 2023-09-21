package com.fitplanner.user.model.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotNull @NotEmpty String email,
        @NotNull @NotEmpty @Size(min = 6) String password
) {}
