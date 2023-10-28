package com.fitplanner.authentication.model.training;

public record ExerciseInfo(
        String id,
        String name,
        String link,
        int sets,
        int reps,
        double weight
) {}
