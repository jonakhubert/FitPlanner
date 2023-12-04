package com.fitplanner.user.controller;

import com.fitplanner.user.model.api.ConfirmationResponse;
import com.fitplanner.user.model.api.UserDetailsRequest;
import com.fitplanner.user.model.food.MealPlan;
import com.fitplanner.user.model.training.WorkoutPlan;
import com.fitplanner.user.model.user.UserDTO;
import com.fitplanner.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/user-management")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/users/{email}/password-change", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConfirmationResponse> changePassword(@PathVariable("email") String email,
        @RequestBody String newPassword
    ) {
        return ResponseEntity.ok(userService.changePassword(email, newPassword));
    }

    @PostMapping(path = "/users/{email}/account-deletion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConfirmationResponse> deleteAccount(@PathVariable("email") String email) {
        return ResponseEntity.ok(userService.deleteAccount(email));
    }

    @GetMapping(path = "/users/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUser(@PathVariable("email") String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @PostMapping(path = "/users/{email}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConfirmationResponse> updateUserDetails(@PathVariable("email") String email,
        @RequestBody @Valid UserDetailsRequest request
    ) {
        return ResponseEntity.ok(userService.updateUserDetails(email, request));
    }

    @PostMapping(path = "/users/{email}/details/nutrition/meal-plans", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveUserMealPlanList(@PathVariable("email") String email,
        @RequestBody List<MealPlan> mealPlanList
    ) {
        userService.saveUserMealPlanList(email, mealPlanList);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(path = "/users/{email}/details/workout/workout-plans", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveUserWorkoutPlanList(@PathVariable("email") String email,
        @RequestBody List<WorkoutPlan> workoutPlanList
    ) {
        userService.saveUserWorkoutPlanList(email, workoutPlanList);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
