package com.fitplanner.user.model.food;

import java.util.List;

public record DailyMealPlan(
        String date,
        List<Meal> meals
) {}