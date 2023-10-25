package com.fitplanner.workout.model;

import com.fitplanner.workout.model.training.WorkoutPlan;

import java.util.List;

public class User {

    private String email;
    private List<WorkoutPlan> workoutPlans;

    // getters
    public String getEmail() { return email; }
    public List<WorkoutPlan> getWorkoutPlans() { return workoutPlans; }

    // setters
    public void setEmail(String email) { this.email = email; }
    public void setWorkoutPlans(List<WorkoutPlan> workoutPlans) { this.workoutPlans = workoutPlans; }
}
