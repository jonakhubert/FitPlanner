package com.fitplanner.nutrition.model;

import java.util.List;

public record Meal(
        String name,
        List<FoodItem> foodItems
) {}
