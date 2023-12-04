package com.fitplanner.nutrition.service;

import com.fitplanner.nutrition.client.UserServiceClient;
import com.fitplanner.nutrition.model.api.ConfirmationResponse;
import com.fitplanner.nutrition.model.api.ProductRequest;
import com.fitplanner.nutrition.model.food.FoodItem;
import com.fitplanner.nutrition.model.food.MealPlan;
import com.fitplanner.nutrition.model.food.Product;
import com.fitplanner.nutrition.model.user.NutritionInfo;
import com.fitplanner.nutrition.model.user.User;
import com.fitplanner.nutrition.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NutritionServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private NutritionService underTest;

    @Test
    public void addFoodItem_ValidParameters_ConfirmationResponse() {
        // given
        var email = "any@gmail.com";
        var date = "2023-10-01";
        var mealName = "Lunch";
        var foodItem = new FoodItem();
        var header = "header";
        var user = new User(email, new NutritionInfo(), new ArrayList<>(), new ArrayList<>());
        var expected = new ConfirmationResponse("Food item has been added.");

        when(userServiceClient.getUser(email, header)).thenReturn(user);

        // when
        var result = underTest.addFoodItem(email, date, mealName, foodItem, header);

        // then
        assertEquals(expected, result);
        verify(userServiceClient, times(1)).getUser(eq(email), eq(header));
        verify(userServiceClient, times(1)).saveUserMealPlanList(eq(email), eq(user.getMealPlanList()), eq(header));
    }

    @Test
    public void removeFoodItem_ValidParameters_ConfirmationResponse() {
        // given
        var email = "any@gmail.com";
        var date = "2023-10-01";
        var mealName = "Lunch";
        var foodItemId = "id";
        var header = "header";
        var user = new User(email, new NutritionInfo(), new ArrayList<>(), new ArrayList<>());
        var expected = new ConfirmationResponse("Food item has been removed.");

        when(userServiceClient.getUser(email, header)).thenReturn(user);

        // when
        var result = underTest.removeFoodItem(email, date, mealName, foodItemId, header);

        // then
        assertEquals(expected, result);
        verify(userServiceClient, times(1)).getUser(eq(email), eq(header));
        verify(userServiceClient, times(1)).saveUserMealPlanList(eq(email), eq(user.getMealPlanList()), eq(header));
    }

    @Test
    public void getMealPlan_ValidParameters_MealPlan() {
        // given
        var email = "any@gmail.com";
        var date = "2023-10-01";
        var header = "header";
        var mealPlan = new MealPlan(date, new ArrayList<>(), 10, 10, 10, 10);
        var mealPlanList = new ArrayList<MealPlan>();
        mealPlanList.add(mealPlan);
        var user = new User(email, new NutritionInfo(), new ArrayList<>(), mealPlanList);

        when(userServiceClient.getUser(email, header)).thenReturn(user);

        // when
        var result = underTest.getMealPlan(email, date, header);

        // then
        assertEquals(mealPlan, result);
        verify(userServiceClient, times(1)).getUser(eq(email), eq(header));
    }

    @Test
    public void getProducts_ExistingName_ProductList() {
        // given
        var name = "name";
        var product1 = new Product(name, 10, 10, 10, 10);
        var product2 = new Product(name + "a", 10, 10, 10, 10);
        var expected = new ArrayList<Product>();
        expected.add(product1);
        expected.add(product2);

        when(productRepository.findByNameIgnoreCase(name)).thenReturn(Optional.of(expected));

        // when
        var result = underTest.getProducts(name);

        // then
        assertEquals(expected, result);
        verify(productRepository, times(1)).findByNameIgnoreCase(eq(name));
    }

    @Test
    public void getProducts_NonExistingName_EmptyList() {
        // given
        var name = "name";

        when(productRepository.findByNameIgnoreCase(name)).thenReturn(Optional.of(Collections.emptyList()));

        // when
        var result = underTest.getProducts(name);

        // then
        assertEquals(Collections.emptyList(), result);
        verify(productRepository, times(1)).findByNameIgnoreCase(eq(name));
    }

    @Test
    public void addProduct_ProductRequest_SavedProduct() {
        // given
        var productRequest = new ProductRequest("any", 10, 10, 10, 10);

        // when
        underTest.addProduct(productRequest);

        // then
        verify(productRepository, times(1)).save(any(Product.class));
    }
}
