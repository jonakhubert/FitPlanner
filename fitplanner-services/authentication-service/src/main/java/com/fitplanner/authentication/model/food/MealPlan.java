package com.fitplanner.authentication.model.food;

import java.util.List;

public class MealPlan {

    private String date;
    private List<Meal> mealList;
    private int dailyCalories;
    private double dailyProtein;
    private double dailyFat;
    private double dailyCarbs;

    public MealPlan() {}

    // getters
    public String getDate() { return date; }
    public List<Meal> getMealList() { return mealList; }
    public int getDailyCalories() { return dailyCalories; }
    public double getDailyProtein() { return dailyProtein; }
    public double getDailyFat() { return dailyFat; }
    public double getDailyCarbs() { return dailyCarbs; }
}
