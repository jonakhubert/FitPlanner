package com.fitplanner.workout.model.api;

import com.fitplanner.workout.model.training.exercise.ExerciseType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ExerciseRequest(
        @NotNull @NotEmpty String name,
        String link,
        @NotNull ExerciseType type
) {}
