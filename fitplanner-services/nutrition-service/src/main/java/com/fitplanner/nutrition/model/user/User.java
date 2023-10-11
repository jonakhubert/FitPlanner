package com.fitplanner.nutrition.model.user;

import com.fitplanner.nutrition.model.food.DailyMealPlan;

import java.util.List;

public class User {

    private String email;
    // TODO: LocalDateTime instead of string
    private NutritionInfo nutritionInfo;
    private List<DailyMealPlan> dailyMealPlans;

    // getters
    public String getEmail() { return email; }
    public NutritionInfo getNutritionInfo() { return nutritionInfo; }
    public List<DailyMealPlan> getDailyMealPlans() { return dailyMealPlans; }

    // setters
    public void setEmail(String email) { this.email = email; }
    public void setDailyMealPlans(List<DailyMealPlan> dailyMealPlans) { this.dailyMealPlans = dailyMealPlans; }
}
