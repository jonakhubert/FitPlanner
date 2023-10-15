package com.fitplanner.user.model.user;

public record NutritionInfo(
        int calories,
        double protein,
        double fat,
        double carbs,
        double height,
        double weight,
        int goal,
        int activity_level
) {}
