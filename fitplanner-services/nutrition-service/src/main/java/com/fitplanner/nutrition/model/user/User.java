package com.fitplanner.nutrition.model.user;

import com.fitplanner.nutrition.model.food.DailyMealPlan;

import java.util.List;

public class User {

    private String email;
    // TODO: create UserPreferences class, LocalDateTime instead of string, remove daily meal plan if empty
    private int calories;
    private double protein;
    private double fat;
    private double carbs;
    private double height;
    private double weight;
    private int goal;
    private int activity_level;
    private List<DailyMealPlan> dailyMealPlans;

    // getters
    public String getEmail() { return email; }
    public List<DailyMealPlan> getDailyMealPlans() { return dailyMealPlans; }
    public int getCalories() { return calories; }
    public double getProtein() { return protein; }
    public double getFat() { return fat; }
    public double getCarbs() { return carbs; }

    // setters
    public void setEmail(String email) { this.email = email; }
    public void setCalories(int calories) { this.calories = calories; }
    public void setDailyMealPlans(List<DailyMealPlan> dailyMealPlans) { this.dailyMealPlans = dailyMealPlans; }
}
