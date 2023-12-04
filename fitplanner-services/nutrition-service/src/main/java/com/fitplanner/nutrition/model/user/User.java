package com.fitplanner.nutrition.model.user;

import com.fitplanner.nutrition.model.food.MealPlan;

import java.util.List;

public class User {

    private String email;
    private NutritionInfo nutritionInfo;
    private List<NutritionInfo> historicalNutritionInfoList;
    private List<MealPlan> mealPlanList;

    public User(String email, NutritionInfo nutritionInfo, List<NutritionInfo> historicalNutritionInfoList,
        List<MealPlan> mealPlanList
    ) {
        this.email = email;
        this.nutritionInfo = nutritionInfo;
        this.historicalNutritionInfoList = historicalNutritionInfoList;
        this.mealPlanList = mealPlanList;
    }

    public User() {}

    // getters
    public String getEmail() { return email; }
    public NutritionInfo getNutritionInfo() { return nutritionInfo; }
    public List<NutritionInfo> getHistoricalNutritionInfoList() { return historicalNutritionInfoList; }
    public List<MealPlan> getMealPlanList() { return mealPlanList; }

    // setters
    public void setEmail(String email) { this.email = email; }
    public void setMealPlanList(List<MealPlan> mealPlanList) { this.mealPlanList = mealPlanList; }
}
