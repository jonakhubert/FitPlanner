package com.fitplanner.nutrition.model.user;

import com.fitplanner.nutrition.model.food.DailyMealPlan;

import java.util.List;

public class User {

    private String email;
    private double calories;
    private double height;
    private double weight;
    private int goal;
    private List<DailyMealPlan> dailyMealPlans;

    // getters
    public String getEmail() { return email; }
    public List<DailyMealPlan> getDailyMealPlans() { return dailyMealPlans; }
    public double getCalories() { return calories; }

    // setters
    public void setEmail(String email) { this.email = email; }
    public void setCalories(double calories) { this.calories = calories; }
    public void setDailyMealPlans(List<DailyMealPlan> dailyMealPlans) { this.dailyMealPlans = dailyMealPlans; }
}
