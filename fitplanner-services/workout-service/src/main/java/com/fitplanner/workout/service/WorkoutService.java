package com.fitplanner.workout.service;

import com.fitplanner.workout.client.UserServiceClient;
import com.fitplanner.workout.model.api.ConfirmationResponse;
import com.fitplanner.workout.model.api.ExerciseRequest;
import com.fitplanner.workout.model.training.Exercise;
import com.fitplanner.workout.model.training.ExerciseInfo;
import com.fitplanner.workout.model.training.WorkoutPlan;
import com.fitplanner.workout.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class WorkoutService {

    private final ExerciseRepository exerciseRepository;
    private final UserServiceClient userServiceClient;

    @Autowired
    public WorkoutService(ExerciseRepository exerciseRepository, UserServiceClient userServiceClient) {
        this.exerciseRepository = exerciseRepository;
        this.userServiceClient = userServiceClient;
    }

    public ConfirmationResponse addExerciseInfo(String email, String date, ExerciseInfo exerciseInfo, String header) {
        var user = userServiceClient.getUser(email, header);

        var workoutPlan = user.getWorkoutPlanList().stream()
            .filter(plan -> plan.getDate().equals(date))
            .findFirst()
            .orElseGet(() -> {
               var newPlan = new WorkoutPlan(date);
               user.getWorkoutPlanList().add(newPlan);
               return newPlan;
            });

        workoutPlan.getExerciseInfoList().add(exerciseInfo);
        userServiceClient.saveWorkoutPlanList(email, user.getWorkoutPlanList(), header);

        return new ConfirmationResponse("Exercise has been added.");
    }

    public ConfirmationResponse removeExerciseInfo(String email, String date, String exerciseInfoId, String header) {
        var user = userServiceClient.getUser(email, header);

        var userWorkoutPlan = user.getWorkoutPlanList().stream()
            .filter(plan -> plan.getDate().equals(date))
            .findFirst();

        userWorkoutPlan.ifPresent(workoutPlan -> {
            workoutPlan.getExerciseInfoList().removeIf(exerciseInfo -> exerciseInfo.getId().equals(exerciseInfoId));

            var hasExerciseInfos = user.getWorkoutPlanList().stream()
                .filter(plan -> plan.getDate().equals(date))
                .anyMatch(plan -> !plan.getExerciseInfoList().isEmpty());

            if(!hasExerciseInfos)
                user.getWorkoutPlanList().removeIf(plan -> plan.getDate().equals(date));
        });

        userServiceClient.saveWorkoutPlanList(email, user.getWorkoutPlanList(), header);

        return new ConfirmationResponse("Exercise has been removed.");
    }

    public WorkoutPlan getWorkoutPlan(String email, String date, String header) {
        var user = userServiceClient.getUser(email, header);

        return user.getWorkoutPlanList().stream()
            .filter(plan -> plan.getDate().equals(date))
            .findFirst()
            .orElse(new WorkoutPlan(date));
    }

    public List<Exercise> getExercises(String name) {
        if(name.isEmpty())
            return Collections.emptyList();

        return exerciseRepository.findByNameIgnoreCase(name)
            .orElse(Collections.emptyList());
    }

    public void addExercise(ExerciseRequest exerciseRequest) {
        var exercise = new Exercise(exerciseRequest.name(), exerciseRequest.link());
        exerciseRepository.save(exercise);
    }
}
