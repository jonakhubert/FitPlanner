package com.fitplanner.nutrition.service;

import com.fitplanner.nutrition.client.UserServiceClient;
import com.fitplanner.nutrition.model.api.ConfirmationResponse;
import com.fitplanner.nutrition.model.api.ProductRequest;
import com.fitplanner.nutrition.model.food.MealPlan;
import com.fitplanner.nutrition.model.food.FoodItem;
import com.fitplanner.nutrition.model.food.Meal;
import com.fitplanner.nutrition.model.food.Product;
import com.fitplanner.nutrition.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class NutritionService {

    private final UserServiceClient userServiceClient;
    private final ProductRepository productRepository;

    @Autowired
    public NutritionService(UserServiceClient userServiceClient, ProductRepository productRepository) {
        this.userServiceClient = userServiceClient;
        this.productRepository = productRepository;
    }

    public ConfirmationResponse addFoodItem(String email, String date, String mealName, FoodItem foodItem, String header) {
        var user = userServiceClient.getUser(email, header);

        var nutritionInfo = user.getHistoricalNutritionInfos().stream()
            .filter(info -> info.isDateInRange(date))
            .findFirst().orElseGet(user::getNutritionInfo);

        // if the meal plan with the specific date doesn't exist, create a new one
        var dailyMealPlan = user.getMealPlans().stream()
            .filter(plan -> plan.getDate().equals(date))
            .findFirst()
            .orElseGet(() -> {
                var newPlan = new MealPlan(
                    date, nutritionInfo.getCalories(), nutritionInfo.getProtein(), nutritionInfo.getFat(),
                    nutritionInfo.getCarbs()
                );
                user.getMealPlans().add(newPlan);
                return newPlan;
            });

        // find the meal in the mealPlan with the specified name
        var existingMeal = dailyMealPlan.getMeals().stream()
            .filter(meal -> meal.getName().equals(mealName))
            .findFirst();

        // if the meal already exists, add the new food item to it
        // otherwise create a new meal with the specified name and the new food item
        existingMeal.ifPresentOrElse(
            meal -> meal.getFoodItems().add(foodItem),
            () -> {
                var newMeal = new Meal(mealName);
                newMeal.getFoodItems().add(foodItem);
                dailyMealPlan.getMeals().add(newMeal);
            }
        );

        userServiceClient.saveMealPlans(email, user.getMealPlans(), header);

        return new ConfirmationResponse("Food item has been added.");
    }

    public ConfirmationResponse removeFoodItem(String email, String date, String mealName, String foodId, String header) {
        var user = userServiceClient.getUser(email, header);

        var userMeal = user.getMealPlans().stream()
            .filter(plan -> plan.getDate().equals(date))
            .flatMap(plan -> plan.getMeals().stream())
            .filter(meal -> meal.getName().equals(mealName))
            .findFirst();

        userMeal.ifPresent(meal -> {
            meal.getFoodItems().removeIf(foodItem -> foodItem.getId().equals(foodId));

            // check if any other meals contain food items
            boolean hasFoodItems = user.getMealPlans().stream()
                .filter(plan -> plan.getDate().equals(date))
                .flatMap(plan -> plan.getMeals().stream())
                .anyMatch(otherMeal -> !otherMeal.getFoodItems().isEmpty());

            // if no other meals contain food items, remove the whole daily meal plan
            if(!hasFoodItems)
                user.getMealPlans().removeIf(plan -> plan.getDate().equals(date));
        });

        userServiceClient.saveMealPlans(email, user.getMealPlans(), header);

        return new ConfirmationResponse("Food item has been removed.");
    }

    public MealPlan getMealPlan(String email, String date, String header) {
        var user = userServiceClient.getUser(email, header);

        var nutritionInfo = user.getHistoricalNutritionInfos().stream()
            .filter(info -> info.isDateInRange(date))
            .findFirst().orElseGet(user::getNutritionInfo);

        return user.getMealPlans().stream()
            .filter(plan -> plan.getDate().equals(date))
            .findFirst()
            .orElse(new MealPlan(
                date, new ArrayList<>(), nutritionInfo.getCalories(), nutritionInfo.getProtein(),
                nutritionInfo.getFat(), nutritionInfo.getCarbs())
            );
    }

    public List<Product> getProducts(String name) {
        if(name.isEmpty())
            return Collections.emptyList();

        return productRepository.findByNameIgnoreCase(name)
            .orElse(Collections.emptyList());
    }

    public void addProduct(ProductRequest request) {
        var product = new Product(request.name(), request.calories(), request.protein(), request.fat(), request.carbs());
        productRepository.save(product);
    }
}
