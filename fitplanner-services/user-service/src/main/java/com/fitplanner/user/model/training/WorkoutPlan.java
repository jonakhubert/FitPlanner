package com.fitplanner.user.model.training;

import java.util.List;

public class WorkoutPlan {

    private String date;
    private List<UserStrengthExercise> strengthExerciseList;
    private List<UserCardioExercise> cardioExerciseList;

    public WorkoutPlan() {}

    public WorkoutPlan(String date, List<UserStrengthExercise> strengthExerciseList,
        List<UserCardioExercise> cardioExerciseList
    ) {
        this.date = date;
        this.strengthExerciseList = strengthExerciseList;
        this.cardioExerciseList = cardioExerciseList;
    }

    // getters
    public String getDate() { return date; }
    public List<UserStrengthExercise> getStrengthExerciseList() { return strengthExerciseList; }
    public List<UserCardioExercise> getCardioExerciseList() { return cardioExerciseList; }

    // setters
    public void setDate(String date) { this.date = date; }
    public void setStrengthExerciseList(List<UserStrengthExercise> strengthExerciseList) { this.strengthExerciseList = strengthExerciseList; }
    public void setCardioExerciseList(List<UserCardioExercise> cardioExerciseList) { this.cardioExerciseList = cardioExerciseList; }
}
