package com.fitplanner.workout.client;

import com.fitplanner.workout.model.User;
import com.fitplanner.workout.model.training.WorkoutPlan;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "user-service", url = "http://localhost:8222/api/user-management")
public interface UserServiceClient {

    @GetMapping(path = "/users/{email}")
    User getUser(@PathVariable("email") String email, @RequestHeader("Authorization") String header);

    @PostMapping(path = "/users/{email}/details/workout/workout-plans")
    void saveUserWorkoutPlanList(@PathVariable("email") String email, @RequestBody List<WorkoutPlan> workoutPlanList,
        @RequestHeader("Authorization") String header
    );
}
