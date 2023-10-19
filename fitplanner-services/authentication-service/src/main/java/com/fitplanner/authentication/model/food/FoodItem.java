package com.fitplanner.authentication.model.food;

public record FoodItem(
        String id,
        String name,
        Double calories,
        Double protein,
        Double fat,
        Double carbs,
        Double quantity
) {}
