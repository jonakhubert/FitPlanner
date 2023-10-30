package com.fitplanner.user.model.user;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class NutritionInfo {

    private int calories;
    private int protein;
    private int fat;
    private int carbs;
    private double height;
    private double weight;
    private int goal;
    private int activity_level;
    private String beginDate;
    private String finishDate;

    public NutritionInfo() {}

    public NutritionInfo(int calories, int protein, int fat, int carbs, double height, double weight, int goal,
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

    public NutritionInfo(int calories, int protein, int fat, int carbs, double height, double weight, int goal,
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

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof NutritionInfo other))
            return false;

        return  Objects.equals(calories, other.calories) &&
                Objects.equals(protein, other.protein) &&
                Objects.equals(fat, other.fat) &&
                Objects.equals(carbs, other.carbs) &&
                Objects.equals(weight, other.weight) &&
                Objects.equals(height, other.height) &&
                Objects.equals(goal, other.goal) &&
                Objects.equals(activity_level, other.activity_level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calories, protein, fat, carbs, weight, height, goal, activity_level);
    }
}
