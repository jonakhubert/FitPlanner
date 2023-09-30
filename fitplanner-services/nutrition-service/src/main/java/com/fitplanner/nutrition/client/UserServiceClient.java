package com.fitplanner.nutrition.client;

import com.fitplanner.nutrition.model.user.User;
//import com.fitplanner.nutrition.model.user.UserNutrition;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "user-service", url = "http://localhost:8222/api/user")
public interface UserServiceClient {

    @GetMapping(path = "/get")
    User getUser(@RequestParam("email") String email, @RequestHeader("Authorization") String header);

    @PostMapping(path = "/save-user-nutrition")
    void saveUserNutrition(@RequestBody User user, @RequestHeader("Authorization") String header);
}
