package com.fitplanner.nutrition.controller;

import com.fitplanner.nutrition.model.api.ConfirmationResponse;
import com.fitplanner.nutrition.model.api.ProductRequest;
import com.fitplanner.nutrition.model.food.MealPlan;
import com.fitplanner.nutrition.model.food.FoodItem;
import com.fitplanner.nutrition.model.food.Product;
import com.fitplanner.nutrition.service.NutritionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/nutrition")
public class NutritionController {

    private final NutritionService nutritionService;

    @Autowired
    public NutritionController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @PostMapping(
        path = "/users/{email}/meal-plans/{date}/meals/{meal}/food-items",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConfirmationResponse> addFoodItem(@PathVariable("email") String email,
        @PathVariable("date") String date, @PathVariable("meal") String mealName, @RequestBody FoodItem foodItem,
        @RequestHeader("Authorization") String header
    ) {
        return ResponseEntity.ok(nutritionService.addFoodItem(email, date, mealName, foodItem, header));
    }

    @DeleteMapping(
        path = "/users/{email}/meal-plans/{date}/meals/{meal}/food-items/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConfirmationResponse> removeFoodItem(
        @PathVariable("email") String email, @PathVariable("date") String date, @PathVariable("meal") String mealName,
        @PathVariable("id") String foodId, @RequestHeader("Authorization") String header
    ) {
        return ResponseEntity.ok(nutritionService.removeFoodItem(email, date, mealName, foodId, header));
    }

    @GetMapping(path = "/users/{email}/meal-plans/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MealPlan> getMealPlan(@PathVariable("email") String email, @PathVariable("date") String date,
        @RequestHeader("Authorization") String header
    ) {
        return ResponseEntity.ok(nutritionService.getMealPlan(email, date, header));
    }

    @GetMapping(path = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> getProducts(@RequestParam("name") String name) {
        return ResponseEntity.ok(nutritionService.getProducts(name));
    }

    @PostMapping(path = "/products")
    public ResponseEntity<Void> addProduct(@RequestBody ProductRequest request) {
        nutritionService.addProduct(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
