package com.fitplanner.workout.controller;

import com.fitplanner.workout.model.api.ExerciseRequest;
import com.fitplanner.workout.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/workout")
public class WorkoutController {

    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping(
        path = "/exercises"
    )
    public ResponseEntity<Void> addExercise(@RequestBody @Valid ExerciseRequest request) {
        workoutService.addExercise(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
