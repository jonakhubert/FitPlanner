package com.fitplanner.nutrition.model.user;

public class NutritionInfo {

    private int calories;
    private double protein;
    private double fat;
    private double carbs;
    private double height;
    private double weight;
    private int goal;
    private int activity_level;

    public NutritionInfo() {}

    public NutritionInfo(int calories, double protein, double fat, double carbs,
         double height, double weight, int goal, int activity_level
    ) {
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.height = height;
        this.weight = weight;
        this.goal = goal;
        this.activity_level = activity_level;
    }

    // getters
    public int getCalories() { return calories; }
    public double getProtein() { return protein; }
    public double getFat() { return fat; }
    public double getCarbs() { return carbs; }
}