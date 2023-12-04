package com.fitplanner.nutrition.model.food;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Meal {
    private String name;
    private List<FoodItem> foodItemList;

    public Meal(String name) {
        this.name = name;
        this.foodItemList = new ArrayList<>();
    }

    public Meal() {}

    // getters
    public String getName() { return name; }
    public List<FoodItem> getFoodItemList() { return foodItemList; }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof Meal other))
            return false;

        return  Objects.equals(name, other.name) &&
                Objects.equals(foodItemList, other.foodItemList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, foodItemList);
    }
}
