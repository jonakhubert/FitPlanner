package com.fitplanner.user.model.food;

public record FoodItem(
        String name,
        double calories,
        double protein,
        double fat,
        double carbs,
        double quantity
) {}
