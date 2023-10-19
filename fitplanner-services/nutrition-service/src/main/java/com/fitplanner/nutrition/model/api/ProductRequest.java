package com.fitplanner.nutrition.model.api;

public record ProductRequest(
        String name,
        Double calories,
        Double protein,
        Double fat,
        Double carbs
) {}
