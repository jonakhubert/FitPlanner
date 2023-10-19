package com.fitplanner.nutrition.service;

import com.fitplanner.nutrition.client.UserServiceClient;
import com.fitplanner.nutrition.model.api.ConfirmationResponse;
import com.fitplanner.nutrition.model.api.FoodItemCreationRequest;
import com.fitplanner.nutrition.model.api.FoodItemRemovalRequest;
import com.fitplanner.nutrition.model.food.DailyMealPlan;
import com.fitplanner.nutrition.model.food.Meal;
import com.fitplanner.nutrition.model.food.Product;
import com.fitplanner.nutrition.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public ConfirmationResponse addFoodItem(FoodItemCreationRequest request, String header) {
        var user = userServiceClient.getUser(request.email(), header);

        // if the daily meal plan with the specific date doesn't exist, create a new one
        var dailyMealPlan = user.getDailyMealPlans().stream()
            .filter(plan -> plan.getDate().equals(request.date()))
            .findFirst()
            .orElseGet(() -> {
                var newPlan = new DailyMealPlan(
                    request.date(), request.calories(), request.protein(), request.fat(),
                    request.carbs()
                );
                user.getDailyMealPlans().add(newPlan);
                return newPlan;
            });

        // find the meal in the dailyMealPlan with the specified name
        var existingMeal = dailyMealPlan.getMeals().stream()
            .filter(meal -> meal.getName().equals(request.mealName()))
            .findFirst();

        // if the meal already exists, add the new food item to it
        // otherwise create a new meal with the specified name and the new food item
        existingMeal.ifPresentOrElse(
            meal -> meal.getFoodItems().add(request.foodItem()),
            () -> {
                var newMeal = new Meal(request.mealName());
                newMeal.getFoodItems().add(request.foodItem());
                dailyMealPlan.getMeals().add(newMeal);
            }
        );

        userServiceClient.saveDailyMealPlans(user, header);

        return new ConfirmationResponse("Food item has been added.");
    }

    public ConfirmationResponse removeFoodItem(FoodItemRemovalRequest request, String header) {
        var user = userServiceClient.getUser(request.email(), header);

        var userMeal = user.getDailyMealPlans().stream()
            .filter(plan -> plan.getDate().equals(request.date()))
            .flatMap(plan -> plan.getMeals().stream())
            .filter(meal -> meal.getName().equals(request.mealName()))
            .findFirst();

        userMeal.ifPresent(meal -> {
            meal.getFoodItems().removeIf(foodItem -> foodItem.getId().equals(request.foodId()));

            // check if any other meals contain food items
            boolean hasFoodItems = user.getDailyMealPlans().stream()
                .filter(plan -> plan.getDate().equals(request.date()))
                .flatMap(plan -> plan.getMeals().stream())
                .anyMatch(otherMeal -> !otherMeal.getFoodItems().isEmpty());

            // if no other meals contain food items, remove the whole daily meal plan
            if(!hasFoodItems)
                user.getDailyMealPlans().removeIf(plan -> plan.getDate().equals(request.date()));
        });

        userServiceClient.saveDailyMealPlans(user, header);

        return new ConfirmationResponse("Food item has been removed.");
    }

    public DailyMealPlan getDailyMealPlan(String email, String date, String header) {
        var user = userServiceClient.getUser(email, header);

        var suitableNutritionInfo = user.getHistoricalNutritionInfos().stream()
            .filter(info -> info.isDateInRange(date))
            .findFirst()
            .orElse(null);

        var nutritionInfo = (suitableNutritionInfo != null) ? suitableNutritionInfo : user.getNutritionInfo();

        return user.getDailyMealPlans().stream()
            .filter(plan -> plan.getDate().equals(date))
            .findFirst()
            .orElse(new DailyMealPlan(
                date, new ArrayList<>(), nutritionInfo.getCalories(), nutritionInfo.getProtein(),
                nutritionInfo.getFat(), nutritionInfo.getCarbs())
            );
    }

    public List<Product> getProducts(String name) {
        return productRepository.findByNameIgnoreCase(name)
            .orElseThrow(() -> new RuntimeException("products not found"));
    }
}
