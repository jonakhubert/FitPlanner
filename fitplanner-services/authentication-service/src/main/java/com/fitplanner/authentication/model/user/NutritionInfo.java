package com.fitplanner.authentication.model.user;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NutritionInfo {

    private Integer calories;
    private Integer protein;
    private Integer fat;
    private Integer carbs;
    private Double height;
    private Double weight;
    private Integer goal;
    private Integer activity_level;
    private String beginDate;
    private String finishDate;

    public NutritionInfo(Integer calories, Integer protein, Integer fat, Integer carbs, Double height, Double weight,
         Integer goal, Integer activity_level
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
    public void setCalories(Integer calories) { this.calories = calories; }
    public void setProtein(Integer protein) { this.protein = protein; }
    public void setFat(Integer fat) { this.fat = fat; }
    public void setCarbs(Integer carbs) { this.carbs = carbs; }
}
