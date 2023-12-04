package com.fitplanner.authentication.model.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotNull @NotEmpty String resetPasswordToken,
        @NotNull @NotEmpty @Size(min = 6) String newPassword
) {}
