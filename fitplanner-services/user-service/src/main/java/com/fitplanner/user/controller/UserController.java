package com.fitplanner.user.controller;

import com.fitplanner.user.model.api.ChangePasswordRequest;
import com.fitplanner.user.model.api.ConfirmationResponse;
import com.fitplanner.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
}
