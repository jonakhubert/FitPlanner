package com.fitplanner.user.model.user;

import com.fitplanner.user.model.food.DailyMealPlan;

import java.util.List;

// preferences TODO: add other parameters (height, weight, etc.)
public record UserNutrition(
        String email,
        double calories,
        List<DailyMealPlan> dailyMealPlans
) {}
