package com.fitplanner.workout.model;

import com.fitplanner.workout.model.training.WorkoutPlan;

import java.util.List;

public class User {

    private String email;
    private List<WorkoutPlan> workoutPlanList;

    public User() {}

    public User(String email, List<WorkoutPlan> workoutPlanList) {
        this.email = email;
        this.workoutPlanList = workoutPlanList;
    }

    // getters
    public String getEmail() { return email; }
    public List<WorkoutPlan> getWorkoutPlanList() { return workoutPlanList; }

    // setters
    public void setEmail(String email) { this.email = email; }
    public void setWorkoutPlanList(List<WorkoutPlan> workoutPlanList) { this.workoutPlanList = workoutPlanList; }
}
