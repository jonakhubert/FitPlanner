package com.fitplanner.nutrition.model.food;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(value = "products")
public class Product {

    @Id
    private String id;
    private String name;
    private double calories;
    private double protein;
    private double fat;
    private double carbs;

    public Product(String name, double calories, double protein, double fat, double carbs) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
    }

    // getters
    public String getId() { return id; }
    public String getName() { return name; }
    public double getCalories() { return calories; }
    public double getProtein() { return protein; }
    public double getFat() { return fat; }
    public double getCarbs() { return carbs; }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof Product other))
            return false;

        return  Objects.equals(id, other.id) &&
                Objects.equals(name, other.name) &&
                Objects.equals(calories, other.calories) &&
                Objects.equals(protein, other.protein) &&
                Objects.equals(fat, other.fat) &&
                Objects.equals(carbs, other.carbs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, calories, protein, fat, carbs);
    }
}
