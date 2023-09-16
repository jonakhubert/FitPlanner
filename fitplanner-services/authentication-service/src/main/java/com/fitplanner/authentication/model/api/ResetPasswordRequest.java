package com.fitplanner.authentication.model.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ResetPasswordRequest(
        @NotNull @NotEmpty String email,
        @NotNull @NotEmpty String resetPasswordToken,
        @NotNull @NotEmpty String newPassword
) {}
