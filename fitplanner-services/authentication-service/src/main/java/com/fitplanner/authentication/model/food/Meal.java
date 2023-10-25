package com.fitplanner.authentication.model.food;

import java.util.List;

public record Meal(
        String name,
        List<FoodItem> foodItems
) {}
