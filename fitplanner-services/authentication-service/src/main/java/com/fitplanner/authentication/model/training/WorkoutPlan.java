package com.fitplanner.authentication.model.training;

import java.util.List;

public class WorkoutPlan {

    private String date;
    private List<UserStrengthExercise> strengthExerciseList;
    private List<UserCardioExercise> cardioExerciseList;

    public WorkoutPlan() {}

    // getters
    public String getDate() { return date; }
    public List<UserStrengthExercise> getStrengthExerciseList() { return strengthExerciseList; }
    public List<UserCardioExercise> getCardioExerciseList() { return cardioExerciseList; }
}
