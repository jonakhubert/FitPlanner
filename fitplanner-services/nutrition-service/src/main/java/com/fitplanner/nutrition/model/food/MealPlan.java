package com.fitplanner.nutrition.model.food;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MealPlan {

    private String date;
    private List<Meal> mealList;
    private int dailyCalories;
    private double dailyProtein;
    private double dailyFat;
    private double dailyCarbs;

    public MealPlan(String date, List<Meal> mealList, int dailyCalories, double dailyProtein, double dailyFat, double dailyCarbs) {
        this.date = date;
        this.mealList = mealList;
        this.dailyCalories = dailyCalories;
        this.dailyProtein = dailyProtein;
        this.dailyFat = dailyFat;
        this.dailyCarbs = dailyCarbs;
        initializeMealList();
    }

    public MealPlan(String date, int dailyCalories, double dailyProtein, double dailyFat, double dailyCarbs) {
        this(date, new ArrayList<>(), dailyCalories, dailyProtein, dailyFat, dailyCarbs);
    }

    public MealPlan() {}

    // getters
    public List<Meal> getMealList() { return mealList; }
    public String getDate() { return date; }
    public int getDailyCalories() { return dailyCalories; }
    public double getDailyProtein() { return dailyProtein; }
    public double getDailyFat() { return dailyFat; }
    public double getDailyCarbs() { return dailyCarbs; }

    private void initializeMealList() {
        mealList.add(new Meal("Breakfast"));
        mealList.add(new Meal("Lunch"));
        mealList.add(new Meal("Dinner"));
        mealList.add(new Meal("Snacks"));
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof MealPlan other))
            return false;

        return  Objects.equals(date, other.date) &&
                Objects.equals(mealList, other.mealList) &&
                Objects.equals(dailyCalories, other.dailyCalories) &&
                Objects.equals(dailyProtein, other.dailyProtein) &&
                Objects.equals(dailyFat, other.dailyFat) &&
                Objects.equals(dailyCarbs, other.dailyCarbs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, mealList, dailyCalories, dailyProtein, dailyFat, dailyCarbs);
    }
}
