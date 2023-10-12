package com.fitplanner.nutrition.client;

import com.fitplanner.nutrition.model.user.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "user-service", url = "http://localhost:8222/api/users")
public interface UserServiceClient {

    @GetMapping(path = "/{email}")
    User getUser(@PathVariable("email") String email, @RequestHeader("Authorization") String header);

    @PostMapping(path = "/nutrition")
    void saveUserNutrition(@RequestBody User user, @RequestHeader("Authorization") String header);
}
