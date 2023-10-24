package com.fitplanner.nutrition.model.user;

import com.fitplanner.nutrition.model.food.MealPlan;

import java.util.List;

public class User {

    private String email;
    private NutritionInfo nutritionInfo;
    private List<NutritionInfo> historicalNutritionInfos;
    private List<MealPlan> mealPlans;

    // getters
    public String getEmail() { return email; }
    public NutritionInfo getNutritionInfo() { return nutritionInfo; }
    public List<NutritionInfo> getHistoricalNutritionInfos() { return historicalNutritionInfos; }
    public List<MealPlan> getMealPlans() { return mealPlans; }

    // setters
    public void setEmail(String email) { this.email = email; }
    public void setDailyMealPlans(List<MealPlan> mealPlans) { this.mealPlans = mealPlans; }
}
