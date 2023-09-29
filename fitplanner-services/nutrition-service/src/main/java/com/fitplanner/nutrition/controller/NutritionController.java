package com.fitplanner.nutrition.controller;

import com.fitplanner.nutrition.model.api.MealRequest;
import com.fitplanner.nutrition.service.NutritionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/nutrition")
public class NutritionController {

    private final NutritionService nutritionService;

    @Autowired
    public NutritionController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @GetMapping(path = "/hello")
    public ResponseEntity<Map<String, String>> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello from secure nutrition endpoint.");
        return ResponseEntity.ok(response);
    }

    @PostMapping(
        path = "/add-meal",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> addMeal(@RequestBody MealRequest request) {
        nutritionService.addMeal(request);
        return ResponseEntity.ok("super");
    }
}
