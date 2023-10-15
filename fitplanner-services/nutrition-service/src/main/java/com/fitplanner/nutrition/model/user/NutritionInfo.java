package com.fitplanner.nutrition.model.user;

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
    public String getBeginDate() { return beginDate; }
    public String getFinishDate() { return finishDate; }

    public boolean isDateInRange(String inputDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate providedDate = LocalDate.parse(inputDate, formatter);
        LocalDate startDate = LocalDate.parse(this.beginDate, formatter);
        LocalDate endDate = this.finishDate != null ? LocalDate.parse(this.finishDate, formatter) : null;

        if(endDate != null)
            return providedDate.isEqual(startDate) || (providedDate.isAfter(startDate) && providedDate.isBefore(endDate));
        else
            return providedDate.isEqual(startDate) || providedDate.isAfter(startDate);
    }
}