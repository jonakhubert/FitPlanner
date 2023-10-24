package com.fitplanner.user.model.user;

import com.fitplanner.user.model.food.MealPlan;

import java.util.List;

public record UserDTO(
        String firstName,
        String lastName,
        String email,
        NutritionInfo nutritionInfo,
        List<NutritionInfo> historicalNutritionInfos,
        List<MealPlan> mealPlans
) {}
