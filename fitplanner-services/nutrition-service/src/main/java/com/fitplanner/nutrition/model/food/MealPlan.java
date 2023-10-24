package com.fitplanner.nutrition.model.food;

import java.util.ArrayList;
import java.util.List;

public class MealPlan {

    private String date;
    private List<Meal> meals;
    private int dailyCalories;
    private double dailyProtein;
    private double dailyFat;
    private double dailyCarbs;

    public MealPlan(String date, List<Meal> meals, int dailyCalories, double dailyProtein, double dailyFat, double dailyCarbs) {
        this.date = date;
        this.meals = meals;
        this.dailyCalories = dailyCalories;
        this.dailyProtein = dailyProtein;
        this.dailyFat = dailyFat;
        this.dailyCarbs = dailyCarbs;
        initializeMeals();
    }

    public MealPlan(String date, int dailyCalories, double dailyProtein, double dailyFat, double dailyCarbs) {
        this(date, new ArrayList<>(), dailyCalories, dailyProtein, dailyFat, dailyCarbs);
    }

    public MealPlan() {}

    // getters
    public List<Meal> getMeals() { return meals; }
    public String getDate() { return date; }
    public int getDailyCalories() { return dailyCalories; }
    public double getDailyProtein() { return dailyProtein; }
    public double getDailyFat() { return dailyFat; }
    public double getDailyCarbs() { return dailyCarbs; }

    private void initializeMeals() {
        meals.add(new Meal("Breakfast"));
        meals.add(new Meal("Lunch"));
        meals.add(new Meal("Dinner"));
        meals.add(new Meal("Snacks"));
    }
}
