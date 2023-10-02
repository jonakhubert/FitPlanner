package com.fitplanner.nutrition.model.food;

import java.util.ArrayList;
import java.util.List;

public class DailyMealPlan {

    private String date;
    private List<Meal> meals;

    public DailyMealPlan(String date, List<Meal> meals) {
        this.date = date;
        this.meals = meals;
        initializeMeals();
    }

    public DailyMealPlan(String date) {
        this(date, new ArrayList<>());
    }

    public DailyMealPlan() {}

    // getters
    public List<Meal> getMeals() { return meals; }
    public String getDate() { return date; }

    private void initializeMeals() {
        meals.add(new Meal("Breakfast"));
        meals.add(new Meal("Lunch"));
        meals.add(new Meal("Dinner"));
        meals.add(new Meal("Snacks"));
    }
}
