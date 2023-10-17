package com.fitplanner.nutrition.model.food;

import java.util.Objects;
import java.util.UUID;

public class FoodItem {

    private String id;
    private String name;
    private Double calories;
    private Double protein;
    private Double fat;
    private Double carbs;
    private Double quantity;

    public FoodItem() {
        this.id = UUID.randomUUID().toString();
    }

    // getters
    public String getId() { return id; }
    public String getName() { return name; }
    public double getCalories() { return calories; }
    public double getProtein() { return protein; }
    public double getFat() { return fat; }
    public double getCarbs() { return carbs; }
    public double getQuantity() { return quantity; }

    // setters
    public void setName(String name) { this.name = name; }
    public void setCalories(Double calories) { this.calories = calories; }
    public void setProtein(Double protein) { this.protein = protein; }
    public void setFat(Double fat) { this.fat = fat; }
    public void setCarbs(Double carbs) { this.carbs = carbs; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof FoodItem other))
            return false;

        return  Objects.equals(id, other.id) &&
                Objects.equals(name, other.name) &&
                Objects.equals(calories, other.calories) &&
                Objects.equals(protein, other.protein) &&
                Objects.equals(fat, other.fat) &&
                Objects.equals(carbs, other.carbs) &&
                Objects.equals(quantity, other.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, calories, protein, fat, carbs, quantity);
    }
}
