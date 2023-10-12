package com.fitplanner.nutrition.model.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record FoodItemRemovalRequest(
        @NotNull @NotEmpty String email,
        @NotNull @NotEmpty String date,
        @NotNull @NotEmpty String mealName,
        @NotNull @NotEmpty String foodId
) {}
