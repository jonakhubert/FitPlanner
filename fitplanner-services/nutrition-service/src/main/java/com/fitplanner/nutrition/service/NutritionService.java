package com.fitplanner.nutrition.service;

import com.fitplanner.nutrition.client.UserServiceClient;
import com.fitplanner.nutrition.model.api.ConfirmationResponse;
import com.fitplanner.nutrition.model.api.MealRequest;
import com.fitplanner.nutrition.model.food.DailyMealPlan;
import com.fitplanner.nutrition.model.food.FoodItem;
import com.fitplanner.nutrition.model.food.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;

@Service
public class NutritionService {

    private final UserServiceClient userServiceClient;

    @Autowired
    public NutritionService(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    public ConfirmationResponse addFoodItem(MealRequest request, String header) {
        var user = userServiceClient.getUser(request.email(), header);

        // if the daily meal plan with the specific date doesn't exist, create a new one
        var dailyMealPlan = user.getDailyMealPlans().stream()
            .filter(plan -> plan.getDate().equals(request.date()))
            .findFirst()
            .orElseGet(() -> {
                var newPlan = new DailyMealPlan(request.date(), user.getCalories(), user.getProtein(), user.getFat(), user.getCarbs());
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

        userServiceClient.saveUserNutrition(user, header);

        return new ConfirmationResponse("Food item has been added.");
    }

    public ConfirmationResponse removeFoodItem(MealRequest request, String header) {
        var user = userServiceClient.getUser(request.email(), header);

        var userMeal = user.getDailyMealPlans().stream()
            .filter(plan -> plan.getDate().equals(request.date()))
            .flatMap(plan -> plan.getMeals().stream())
            .filter(meal -> meal.getName().equals(request.mealName()))
            .findFirst();

        userMeal.ifPresent(meal -> {
            var iterator = meal.getFoodItems().iterator();
            while(iterator.hasNext()) {
                var foodItem = iterator.next();
                if(foodItem.equals(request.foodItem())) {
                    iterator.remove();
                    break; // Break the loop after removing the first occurrence
                }
            }
        });

        userServiceClient.saveUserNutrition(user, header);

        return new ConfirmationResponse("Food item has been removed.");
    }

    public DailyMealPlan getDailyMealPlan(String email, String date, String header) {
        var user = userServiceClient.getUser(email, header);

        return user.getDailyMealPlans().stream()
            .filter(plan -> plan.getDate().equals(date))
            .findFirst()
            .orElse(new DailyMealPlan(date, new ArrayList<>(), user.getCalories(), user.getProtein(), user.getFat(), user.getCarbs()));
    }
}
