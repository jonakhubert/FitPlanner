package com.fitplanner.workout.controller;

import com.fitplanner.workout.model.api.ConfirmationResponse;
import com.fitplanner.workout.model.api.StrengthExerciseRequest;
import com.fitplanner.workout.model.training.*;
import com.fitplanner.workout.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/workout")
public class WorkoutController {

    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping(
        path = "/users/{email}/workout-plans/{date}/strength-exercises",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConfirmationResponse> addUserStrengthExercise(@PathVariable("email") String email,
        @PathVariable("date") String date, @RequestBody UserStrengthExercise strengthExercise,
        @RequestHeader("Authorization") String header
    ) {
        return ResponseEntity.ok(workoutService.addUserStrengthExercise(email, date, strengthExercise, header));
    }

    @DeleteMapping(
        path = "/users/{email}/workout-plans/{date}/strength-exercises/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConfirmationResponse> removeUserStrengthExercise(@PathVariable("email") String email,
        @PathVariable("date") String date, @PathVariable("id") String strengthExerciseId,
        @RequestHeader("Authorization") String header
    ) {
        return ResponseEntity.ok(workoutService.removeUserStrengthExercise(email, date, strengthExerciseId, header));
    }

    @PostMapping(
        path = "/users/{email}/workout-plans/{date}/cardio-exercises",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConfirmationResponse> addUserCardioExercise(@PathVariable("email") String email,
        @PathVariable("date") String date, @RequestBody UserCardioExercise cardioExercise,
        @RequestHeader("Authorization") String header
    ) {
        return ResponseEntity.ok(workoutService.addUserCardioExercise(email, date, cardioExercise, header));
    }

    @DeleteMapping(
        path = "/users/{email}/workout-plans/{date}/cardio-exercises/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConfirmationResponse> removeUserCardioExercise(@PathVariable("email") String email,
        @PathVariable("date") String date, @PathVariable("id") String cardioExerciseId,
        @RequestHeader("Authorization") String header
    ) {
        return ResponseEntity.ok(workoutService.removeUserCardioExercise(email, date, cardioExerciseId, header));
    }

    @GetMapping(path = "/users/{email}/workout-plans/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkoutPlan> getUserWorkoutPlan(@PathVariable("email") String email,
        @PathVariable("date") String date, @RequestHeader("Authorization") String header
    ) {
        return ResponseEntity.ok(workoutService.getWorkoutPlan(email, date, header));
    }

    @GetMapping(path = "/strength-exercises")
    public ResponseEntity<List<StrengthExercise>> getStrengthExercises(@RequestParam("name") String name) {
        return ResponseEntity.ok(workoutService.getStrengthExercises(name));
    }

    @PostMapping(path = "/strength-exercises")
    public ResponseEntity<Void> addStrengthExercise(@RequestBody @Valid StrengthExerciseRequest request) {
        workoutService.addStrengthExercise((request));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(path = "/cardio-exercises")
    public ResponseEntity<List<CardioExercise>> getCardioExercises(@RequestParam("name") String name) {
        return ResponseEntity.ok(workoutService.getCardioExercise(name));
    }

    @PostMapping(path = "/cardio-exercises")
    public ResponseEntity<Void> addCardioExercise(@RequestBody String request) {
        workoutService.addCardioExercise(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
