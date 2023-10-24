package com.fitplanner.authentication.model.food;

import java.util.ArrayList;
import java.util.List;

public class DailyMealPlan {

    private String date;
    private List<Meal> meals;
    private int dailyCalories;
    private double dailyProtein;
    private double dailyFat;
    private double dailyCarbs;

    public DailyMealPlan(String date) {
        this.date = date;
        this.meals = new ArrayList<>();
    }

    // getters
    public List<Meal> getMeals() { return meals; }
    public String getDate() { return date; }
    public int getDailyCalories() { return dailyCalories; }
    public double getDailyProtein() { return dailyProtein; }
    public double getDailyFat() { return dailyFat; }
    public double getDailyCarbs() { return dailyCarbs; }
}
