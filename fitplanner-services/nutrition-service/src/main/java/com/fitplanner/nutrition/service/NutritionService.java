package com.fitplanner.nutrition.service;

import com.fitplanner.nutrition.model.api.MealRequest;
import com.fitplanner.nutrition.model.food.DailyMealPlan;
import com.fitplanner.nutrition.model.food.Meal;
import com.fitplanner.nutrition.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NutritionService {

    private final UserRepository userRepository;

    @Autowired
    public NutritionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addMeal(MealRequest request) {
        var user = userRepository.findByEmail(request.email()).orElseThrow(() -> new RuntimeException("Not found"));

        var dailyMealPlan = user.getDailyMealPlans().stream()
            .filter(plan -> plan.getDate().equals(request.date()))
            .findFirst()
            .orElse(null);

        // if the daily meal plan for the specific date doesn't exist, create a new one
        if(dailyMealPlan == null) {
            dailyMealPlan = new DailyMealPlan(request.date());
            user.getDailyMealPlans().add(dailyMealPlan);
            userRepository.save(user);
        }

        // find the meal in the dailyMealPlan with the specified name
        var existingMealOptional = dailyMealPlan.getMeals().stream()
            .filter(meal -> meal.getName().equals(request.mealName()))
            .findFirst();

        // if the meal already exists, add the new food item to it
        if(existingMealOptional.isPresent()) {
            var existingMeal = existingMealOptional.get();
            existingMeal.getFoodItems().add(request.foodItem());
        } else {
            // if the meal doesn't exist, create a new meal with the specified name and the new food item
            var newMeal = new Meal(request.mealName());
            newMeal.getFoodItems().add(request.foodItem());
            dailyMealPlan.getMeals().add(newMeal);
        }

        userRepository.save(user);
    }

//    public void addMeal(MealRequest request) {
//        var user = userRepository.findByEmail(request.email()).orElseThrow(() -> new RuntimeException("Not found"));
//
//        var userMealPlan = user.getDailyMealPlans().stream()
//            .filter(dailyMealPlan -> dailyMealPlan.getDate().equals(request.date()))
//            .findFirst()
//            .orElseGet(() -> {
//                var newPlan = new DailyMealPlan(user, request.date());
//                dailyMealPlanRepository.save(newPlan);
//                user.getDailyMealPlans().add(newPlan);
//                userRepository.save(user);
//                return newPlan;
//            });
//
//        var existingMeal = userMealPlan.getMeals().stream()
//            .filter(meal -> meal.name().equals(request.mealName()))
//            .findFirst()
//            .orElse(null);
//
//        if (existingMeal == null) {
//            existingMeal = new Meal(request.mealName(), new ArrayList<>());
//            userMealPlan.getMeals().add(existingMeal);
//        }
//
//        existingMeal.foodItems().add(request.foodItem());
//        dailyMealPlanRepository.save(userMealPlan);
//    }
}
