package com.fitplanner.authentication.model.food;

public record FoodItem(
        String name,
        double calories,
        double protein,
        double fat,
        double carbs,
        double quantity
) {}
