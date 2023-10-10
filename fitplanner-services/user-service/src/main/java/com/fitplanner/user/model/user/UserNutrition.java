package com.fitplanner.user.model.user;

import com.fitplanner.user.model.food.DailyMealPlan;

import java.util.List;

public record UserNutrition(
        String email,
        double calories,
        List<DailyMealPlan> dailyMealPlans
) {}
