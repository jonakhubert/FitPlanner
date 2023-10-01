package com.fitplanner.nutrition.model.food;

import java.util.ArrayList;
import java.util.List;

public class DailyMealPlan {

    private String date;
    private List<Meal> meals;

    public DailyMealPlan(String date) {
        this.date = date;
        this.meals = new ArrayList<>();
    }

    public DailyMealPlan() {}

    public DailyMealPlan(String date, List<Meal> meals) {
        this.date = date;
        this.meals = meals;
        this.meals.add(new Meal("Breakfast"));
        this.meals.add(new Meal("Lunch"));
        this.meals.add(new Meal("Dinner"));
        this.meals.add(new Meal("Snacks"));
    }

    // getters
    public List<Meal> getMeals() { return meals; }
    public String getDate() { return date; }
}
