package com.fitplanner.user.model.user;

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

    public NutritionInfo() {}

    public NutritionInfo(int calories, double protein, double fat, double carbs, double height, double weight, int goal,
         int activity_level, String beginDate, String finishDate
    ) {
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.height = height;
        this.weight = weight;
        this.goal = goal;
        this.activity_level = activity_level;
        this.beginDate = beginDate;
        this.finishDate = finishDate;
    }

    public NutritionInfo(int calories, double protein, double fat, double carbs, double height, double weight, int goal,
         int activity_level
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

    // getters
    public int getCalories() { return calories; }
    public double getProtein() { return protein; }
    public double getFat() { return fat; }
    public double getCarbs() { return carbs; }
    public double getHeight() { return height; }
    public double getWeight() { return weight; }
    public int getGoal() { return goal; }
    public int getActivity_level() { return activity_level; }
    public String getBeginDate() { return beginDate; }
    public String getFinishDate() { return finishDate; }

    // setters
    public void setFinishDate(String finishDate) { this.finishDate = finishDate; }
}
