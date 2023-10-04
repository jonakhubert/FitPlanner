package com.fitplanner.user.controller;

import com.fitplanner.user.model.api.ChangePasswordRequest;
import com.fitplanner.user.model.api.ConfirmationResponse;
import com.fitplanner.user.model.user.User;
import com.fitplanner.user.model.user.UserNutrition;
import com.fitplanner.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(
        path = "/change-password",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConfirmationResponse> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        return ResponseEntity.ok(userService.changePassword(request));
    }

    @PostMapping(
        path = "/delete-account",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConfirmationResponse> deleteAccount(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.deleteAccount(email));
    }

    @GetMapping(
        path = "/get",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> getUser(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @PostMapping(
        path = "/save-user-nutrition",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> saveUserNutrition(@RequestBody UserNutrition user) {
        userService.saveUserNutrition(user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
