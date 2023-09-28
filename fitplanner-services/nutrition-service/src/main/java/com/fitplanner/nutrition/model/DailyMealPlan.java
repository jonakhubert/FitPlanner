package com.fitplanner.nutrition.model;

import com.fitplanner.nutrition.model.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.List;

@Document(value = "daily_meal_plans")
public class DailyMealPlan {

    @Id
    private String id;
    private LocalDateTime date;
    private List<Meal> meals;

    @DocumentReference(lazy = true)
    private User user;

    // getters
    public List<Meal> getMeals() { return meals; }
}
