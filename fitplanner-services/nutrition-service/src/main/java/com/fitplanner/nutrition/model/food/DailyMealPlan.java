package com.fitplanner.nutrition.model.food;

import com.fitplanner.nutrition.model.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Document(value = "daily_meal_plans")
public class DailyMealPlan {

    @Id
    private String id;
    private String date;
    private List<Meal> meals;

    @DocumentReference(lazy = true)
    private User user;

    public DailyMealPlan(User user, String date) {
        this.date = date;
        this.meals = new ArrayList<>();
        this.user = user;
    }

    // getters
    public List<Meal> getMeals() { return meals; }
    public String getDate() { return date; }
}
