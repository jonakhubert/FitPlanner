package com.fitplanner.authentication.model.training;

public record ExerciseInfo(
        String id,
        String name,
        String link,
        Integer sets,
        Integer reps,
        Double weight
) {}
