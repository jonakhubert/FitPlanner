package com.fitplanner.user.model.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserDetailsRequest(
        @NotNull @NotEmpty String firstName,
        @NotNull @NotEmpty String lastName,
        @NotNull @NotEmpty String email,
        @NotNull Double height,
        @NotNull Double weight,
        @NotNull Integer goal,
        @NotNull Integer activity_level
) {}
