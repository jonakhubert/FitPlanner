package com.fitplanner.workout.model.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ExerciseRequest(
        @NotNull @NotEmpty String name,
        @NotNull @NotEmpty String link
) {}
