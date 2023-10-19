package com.fitplanner.nutrition.model.food;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "products")
public class Product {

    @Id
    private String id;
    private String name;
    private Double calories;
    private Double protein;
    private Double fat;
    private Double carbs;

    public Product(String name, Double calories, Double protein, Double fat, Double carbs) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
    }

    // getters
    public String getId() { return id; }
    public String getName() { return name; }
    public Double getCalories() { return calories; }
    public Double getProtein() { return protein; }
    public Double getFat() { return fat; }
    public Double getCarbs() { return carbs; }
}
