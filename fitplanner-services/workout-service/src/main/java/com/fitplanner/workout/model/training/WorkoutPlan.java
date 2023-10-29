package com.fitplanner.workout.model.training;

import java.util.ArrayList;
import java.util.List;

public class WorkoutPlan {

    private String date;
    private List<UserStrengthExercise> strengthExerciseList;

    public WorkoutPlan(String date) {
        this.date = date;
        this.strengthExerciseList = new ArrayList<>();
    }

    public WorkoutPlan(String date, List<UserStrengthExercise> strengthExerciseList) {
        this.date = date;
        this.strengthExerciseList = strengthExerciseList;
    }

    public WorkoutPlan() {}

    // getters
    public String getDate() { return date; }
    public List<UserStrengthExercise> getStrengthExerciseList() { return strengthExerciseList; }

    // setters
    public void setDate(String date) { this.date = date; }
    public void setStrengthExerciseList(List<UserStrengthExercise> strengthExerciseList) { this.strengthExerciseList = strengthExerciseList; }
}
