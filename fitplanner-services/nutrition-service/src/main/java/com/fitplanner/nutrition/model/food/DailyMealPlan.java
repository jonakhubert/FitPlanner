package com.fitplanner.nutrition.model.food;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class DailyMealPlan {

    @Id
    private String id;
    private String date;
    private List<Meal> meals;

    public DailyMealPlan(String date) {
        this.date = date;
        this.meals = new ArrayList<>();
    }

    // getters
    public List<Meal> getMeals() { return meals; }
    public String getDate() { return date; }
}
