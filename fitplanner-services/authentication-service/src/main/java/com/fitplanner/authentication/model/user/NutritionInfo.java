package com.fitplanner.authentication.model.user;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NutritionInfo {

    private int calories;
    private double protein;
    private double fat;
    private double carbs;
    private double height;
    private double weight;
    private int goal;
    private int activity_level;
    private String beginDate;
    private String finishDate;

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
        this.beginDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    // setters
    public void setCalories(int calories) { this.calories = calories; }
    public void setProtein(double protein) { this.protein = protein; }
    public void setFat(double fat) { this.fat = fat; }
    public void setCarbs(double carbs) { this.carbs = carbs; }
}
