package com.fitplanner.nutrition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitplanner.nutrition.model.api.ConfirmationResponse;
import com.fitplanner.nutrition.model.api.ProductRequest;
import com.fitplanner.nutrition.model.food.FoodItem;
import com.fitplanner.nutrition.model.food.MealPlan;
import com.fitplanner.nutrition.model.food.Product;
import com.fitplanner.nutrition.service.NutritionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NutritionController.class)
public class NutritionControllerTest {

    @MockBean
    private NutritionService nutritionService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = "/api/nutrition";

    @Test
    public void addFoodItem_ValidParameters_ConfirmationResponse() throws Exception {
        // given
        var email = "any@gmail.com";
        var date = "2023-10-11";
        var mealName = "Lunch";
        var header = "Bearer token";
        var foodItem = new FoodItem();
        var confirmationResponse = new ConfirmationResponse("Food item has been added.");

        when(nutritionService.addFoodItem(eq(email), eq(date), eq(mealName), eq(foodItem), eq(header)))
            .thenReturn(confirmationResponse);

        // then
        mockMvc.perform(post(baseUrl + "/users/{email}/meal-plans/{date}/meals/{meal}/food-items", email, date, mealName)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", header)
            .content(objectMapper.writeValueAsString(foodItem)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.confirmation_message").value("Food item has been added."));
    }

    @Test
    public void removeFoodItem_ValidParameters_ConfirmationResponse() throws Exception {
        // given
        var email = "any@gmail.com";
        var date = "2023-10-11";
        var mealName = "Lunch";
        var header = "Bearer token";
        var foodId = "id";
        var confirmationResponse = new ConfirmationResponse("Food item has been removed.");

        when(nutritionService.removeFoodItem(eq(email), eq(date), eq(mealName), eq(foodId), eq(header)))
            .thenReturn(confirmationResponse);

        // then
        mockMvc.perform(delete(baseUrl + "/users/{email}/meal-plans/{date}/meals/{meal}/food-items/{id}", email, date,
            mealName, foodId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", header))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.confirmation_message").value("Food item has been removed."));
    }

    @Test
    public void getMealPlan_ValidParameters_MealPlan() throws Exception {
        // given
        var email = "any@gmail.com";
        var date = "2023-10-11";
        var header = "Bearer token";
        var mealPlan = new MealPlan(date, 3000, 250, 100, 340);

        when(nutritionService.getMealPlan(eq(email), eq(date), eq(header))).thenReturn(mealPlan);

        // then
        mockMvc.perform(get(baseUrl + "/users/{email}/meal-plans/{date}", email, date)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.date").value(date))
            .andExpect(jsonPath("$.mealList[0].name").value("Breakfast"))
            .andExpect(jsonPath("$.mealList[0].foodItemList").isEmpty())
            .andExpect(jsonPath("$.mealList[1].name").value("Lunch"))
            .andExpect(jsonPath("$.mealList[1].foodItemList").isEmpty())
            .andExpect(jsonPath("$.mealList[2].name").value("Dinner"))
            .andExpect(jsonPath("$.mealList[2].foodItemList").isEmpty())
            .andExpect(jsonPath("$.mealList[3].name").value("Snacks"))
            .andExpect(jsonPath("$.mealList[0].foodItemList").isEmpty());
    }

    @Test
    public void getProducts_ExistingName_ProductList() throws Exception {
        // given
        var name = "name";
        var productList = new ArrayList<Product>();
        productList.add(new Product("p1", 10, 10, 10, 10));
        productList.add(new Product("p2", 10, 10, 10, 10));

        when(nutritionService.getProducts(name)).thenReturn(productList);

        // then
        mockMvc.perform(get(baseUrl + "/products")
            .param("name", name)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("p1"))
            .andExpect(jsonPath("$[1].name").value("p2"));
    }

    @Test
    public void addProduct_ValidRequest_StatusOk() throws Exception {
        // given
        var request = new ProductRequest("p1", 10, 10, 10, 10);

        // then
        mockMvc.perform(post(baseUrl + "/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }
}
