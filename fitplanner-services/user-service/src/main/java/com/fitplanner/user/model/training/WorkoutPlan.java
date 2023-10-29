package com.fitplanner.user.model.training;

import java.util.List;

public class WorkoutPlan {

    private String date;
    private List<UserStrengthExercise> strengthExerciseList;

    public WorkoutPlan() {}

    public WorkoutPlan(String date, List<UserStrengthExercise> strengthExerciseList) {
        this.date = date;
        this.strengthExerciseList = strengthExerciseList;
    }

    // getters
    public String getDate() { return date; }
    public List<UserStrengthExercise> getStrengthExerciseList() { return strengthExerciseList; }

    // setters
    public void setDate(String date) { this.date = date; }
    public void setStrengthExerciseList(List<UserStrengthExercise> strengthExerciseList) { this.strengthExerciseList = strengthExerciseList; }
}
