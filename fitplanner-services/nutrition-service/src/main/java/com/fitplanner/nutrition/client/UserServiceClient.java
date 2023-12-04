package com.fitplanner.nutrition.client;

import com.fitplanner.nutrition.model.food.MealPlan;
import com.fitplanner.nutrition.model.user.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "user-service", url = "http://localhost:8222/api/user-management")
public interface UserServiceClient {

    @GetMapping(path = "/users/{email}")
    User getUser(@PathVariable("email") String email, @RequestHeader("Authorization") String header);

    @PostMapping(path = "/users/{email}/details/nutrition/meal-plans")
    void saveUserMealPlanList(@PathVariable("email") String email, @RequestBody List<MealPlan> mealPlanList,
        @RequestHeader("Authorization") String header);
}
