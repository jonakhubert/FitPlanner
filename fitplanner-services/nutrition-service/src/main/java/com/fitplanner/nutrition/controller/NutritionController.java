package com.fitplanner.nutrition.controller;

import com.fitplanner.nutrition.model.api.ConfirmationResponse;
import com.fitplanner.nutrition.model.api.FoodItemCreationRequest;
import com.fitplanner.nutrition.model.api.FoodItemRemovalRequest;
import com.fitplanner.nutrition.model.food.DailyMealPlan;
import com.fitplanner.nutrition.model.food.Product;
import com.fitplanner.nutrition.service.NutritionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
        path = "/food-items",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConfirmationResponse> addFoodItem(
        @RequestBody @Valid FoodItemCreationRequest request,
        @RequestHeader("Authorization") String header
    ) {
        return ResponseEntity.ok(nutritionService.addFoodItem(request, header));
    }

    @DeleteMapping(
        path = "/food-items",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConfirmationResponse> removeFoodItem(
        @RequestBody @Valid FoodItemRemovalRequest request,
        @RequestHeader("Authorization") String header
    ) {
        return ResponseEntity.ok(nutritionService.removeFoodItem(request, header));
    }

    @GetMapping(
        path = "/daily-meal-plans",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<DailyMealPlan> getDailyMealPlan(
        @RequestParam("email") String email,
        @RequestParam("date") String date,
        @RequestHeader("Authorization") String header
    ) {
        return ResponseEntity.ok(nutritionService.getDailyMealPlan(email, date, header));
    }

    @GetMapping(
        path = "/products",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Product>> getProducts(@RequestParam("name") String name) {
        return ResponseEntity.ok(nutritionService.getProducts(name));
    }

//    @PostMapping(
//        path = "/product"
//    )
//    public void addProduct(@RequestBody ProductRequest request) {
//        var product = new Product(request.name(), request.calories(), request.protein(), request.fat(), request.carbs());
//        productRepository.save(product);
//    }
}
