package com.fitplanner.nutrition.model;

public record FoodItem(
        String name,
        double calories,
        double protein,
        double fat,
        double carbs,
        double quantity
) {}
