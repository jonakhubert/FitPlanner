package com.fitplanner.nutrition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitplanner.nutrition.model.api.ConfirmationResponse;
import com.fitplanner.nutrition.model.food.FoodItem;
import com.fitplanner.nutrition.service.NutritionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    public void test() throws Exception {
        var foodItem = new FoodItem();
        var confirmationResponse = new ConfirmationResponse("test");

        when(nutritionService.addFoodItem(any(), any(), any(), any(), any())).thenReturn(confirmationResponse);

        mockMvc.perform(post(baseUrl + "/users/{email}/meal-plans/{date}/meals/{meal}/food-items", "any@gmail.com", "2023-10-11", "Lunch")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer token")
                .content(objectMapper.writeValueAsString(foodItem)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
