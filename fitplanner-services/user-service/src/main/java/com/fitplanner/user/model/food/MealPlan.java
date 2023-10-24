package com.fitplanner.user.model.food;

import java.util.List;

public class MealPlan {

    private String date;
    private List<Meal> meals;
    private int dailyCalories;
    private double dailyProtein;
    private double dailyFat;
    private double dailyCarbs;

    public MealPlan() {}

    // getters
    public String getDate() { return date; }
    public List<Meal> getMeals() { return meals; }
    public int getDailyCalories() { return dailyCalories; }
    public double getDailyProtein() { return dailyProtein; }
    public double getDailyFat() { return dailyFat; }
    public double getDailyCarbs() { return dailyCarbs; }
}
