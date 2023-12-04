package com.fitplanner.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitplanner.user.model.api.ConfirmationResponse;
import com.fitplanner.user.model.api.UserDetailsRequest;
import com.fitplanner.user.model.food.MealPlan;
import com.fitplanner.user.model.training.WorkoutPlan;
import com.fitplanner.user.model.user.NutritionInfo;
import com.fitplanner.user.model.user.UserDTO;
import com.fitplanner.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = "/api/user-management";

    @Test
    public void changePassword_ValidParameters_ConfirmationResponse() throws Exception {
        // given
        var email = "any@gmail.com";
        var password = "newPassword";
        var confirmationResponse = new ConfirmationResponse("Password has been changed.");

        when(userService.changePassword(email, password)).thenReturn(confirmationResponse);

        // then
        mockMvc.perform(post(baseUrl + "/users/{email}/password-change", email)
            .contentType(MediaType.TEXT_PLAIN_VALUE)
            .content(password))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.confirmation_message").value("Password has been changed."));
    }

    @Test
    public void deleteAccount_ExistingEmail_ConfirmationResponse() throws Exception {
        // given
        var email = "any@gmail.com";
        var confirmationResponse = new ConfirmationResponse("Account has been deleted.");

        when(userService.deleteAccount(email)).thenReturn(confirmationResponse);

        // then
        mockMvc.perform(post(baseUrl + "/users/{email}/account-deletion", email))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.confirmation_message").value("Account has been deleted."));
    }

    @Test
    public void getUser_ExistingEmail_UserDTO() throws Exception {
        // given
        var firstName = "any";
        var lastName = "any";
        var email = "any@gmail.com";
        var userDTO = new UserDTO(firstName, lastName, email, new NutritionInfo(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        when(userService.findUserByEmail(eq(email))).thenReturn(userDTO);

        // then
        mockMvc.perform(get(baseUrl + "/users/{email}", email)
            .contentType(MediaType.TEXT_PLAIN_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value(firstName))
            .andExpect(jsonPath("$.lastName").value(lastName))
            .andExpect(jsonPath("$.email").value(email))
            .andExpect(jsonPath("$.mealPlanList").isEmpty())
            .andExpect(jsonPath("$.workoutPlanList").isEmpty());
    }

    @Test
    public void updateUserDetails_ValidParameters_ConfirmationResponse() throws Exception {
        // given
        var firstName = "any";
        var lastName = "any";
        var email = "any@gmail.com";
        var request = new UserDetailsRequest(firstName, lastName, 191, 94, 3, 3);
        var expected = new ConfirmationResponse("User updated successfully.");

        when(userService.updateUserDetails(eq(email), eq(request))).thenReturn(expected);

        // then
        mockMvc.perform(post(baseUrl + "/users/{email}/details", email)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.confirmation_message").value("User updated successfully."));
    }

    @Test
    public void saveUserMealPlanList_ValidParameters_Status200() throws Exception {
        // given
        var email = "any@gmail.com";
        var mealPlanList = new ArrayList<MealPlan>();

        // then
        mockMvc.perform(post(baseUrl + "/users/{email}/details/nutrition/meal-plans", email)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(mealPlanList)))
            .andExpect(status().isOk());
    }

    @Test
    public void saveUserWorkoutPlanList_ValidParameters_Status200() throws Exception {
        // given
        var email = "any@gmail.com";
        var workoutPlanList = new ArrayList<WorkoutPlan>();

        // then
        mockMvc.perform(post(baseUrl + "/users/{email}/details/workout/workout-plans", email)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(workoutPlanList)))
            .andExpect(status().isOk());
    }
}
