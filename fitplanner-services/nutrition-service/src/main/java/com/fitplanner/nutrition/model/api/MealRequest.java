package com.fitplanner.nutrition.model.api;

import com.fitplanner.nutrition.model.food.FoodItem;

public record MealRequest(
        String email,
        String date,
        String mealName,
        FoodItem foodItem
) {}
