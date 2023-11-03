package com.fitplanner.nutrition.model.api;

public record ProductRequest(
        String name,
        double calories,
        double protein,
        double fat,
        double carbs
) {}
