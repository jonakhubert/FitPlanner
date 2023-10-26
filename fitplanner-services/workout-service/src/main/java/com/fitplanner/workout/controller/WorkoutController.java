package com.fitplanner.workout.controller;

import com.fitplanner.workout.model.api.ConfirmationResponse;
import com.fitplanner.workout.model.api.ExerciseRequest;
import com.fitplanner.workout.model.training.Exercise;
import com.fitplanner.workout.model.training.ExerciseInfo;
import com.fitplanner.workout.model.training.WorkoutPlan;
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
        path = "users/{email}/workout-plans/{date}/exercises",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConfirmationResponse> addExerciseInfo(
        @PathVariable("email") String email, @PathVariable("date") String date, @RequestBody ExerciseInfo exerciseInfo,
        @RequestHeader("Authorization") String header
    ) {
        return ResponseEntity.ok(workoutService.addExerciseInfo(email, date, exerciseInfo, header));
    }

    @DeleteMapping(
        path = "users/{email}/workout-plans/{date}/exercises/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConfirmationResponse> removeExerciseInfo(
        @PathVariable("email") String email, @PathVariable("date") String date, @PathVariable("id") String exerciseInfoId,
        @RequestHeader("Authorization") String header
    ) {
        return ResponseEntity.ok(workoutService.removeExerciseInfo(email, date, exerciseInfoId, header));
    }

    @GetMapping(
        path = "users/{email}/workout-plans/{date}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WorkoutPlan> getWorkoutPlan(
        @PathVariable("email") String email, @PathVariable("date") String date,
        @RequestHeader("Authorization") String header
    ) {
        return ResponseEntity.ok(workoutService.getWorkoutPlan(email, date, header));
    }

    @GetMapping(path = "/exercises")
    public ResponseEntity<List<Exercise>> getExercises(@RequestParam("name") String name) {
        return ResponseEntity.ok(workoutService.getExercises(name));
    }

    @PostMapping(
        path = "/exercises"
    )
    public ResponseEntity<Void> addExercise(@RequestBody @Valid ExerciseRequest request) {
        workoutService.addExercise(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
