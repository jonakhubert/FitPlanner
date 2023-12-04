package com.fitplanner.workout.model.training;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkoutPlan {

    private String date;
    private List<UserStrengthExercise> strengthExerciseList;
    private List<UserCardioExercise> cardioExerciseList;

    public WorkoutPlan(String date) {
        this.date = date;
        this.strengthExerciseList = new ArrayList<>();
        this.cardioExerciseList = new ArrayList<>();
    }

    public WorkoutPlan(String date, List<UserStrengthExercise> strengthExerciseList,
        List<UserCardioExercise> cardioExerciseList
    ) {
        this.date = date;
        this.strengthExerciseList = strengthExerciseList;
        this.cardioExerciseList = cardioExerciseList;
    }

    public WorkoutPlan() {}

    // getters
    public String getDate() { return date; }
    public List<UserStrengthExercise> getStrengthExerciseList() { return strengthExerciseList; }
    public List<UserCardioExercise> getCardioExerciseList() { return cardioExerciseList; }

    // setters
    public void setDate(String date) { this.date = date; }
    public void setStrengthExerciseList(List<UserStrengthExercise> strengthExerciseList) { this.strengthExerciseList = strengthExerciseList; }
    public void setCardioExerciseList(List<UserCardioExercise> cardioExerciseList) { this.cardioExerciseList = cardioExerciseList; }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof WorkoutPlan other))
            return false;

        return  Objects.equals(date, other.date) &&
                Objects.equals(strengthExerciseList, other.strengthExerciseList) &&
                Objects.equals(cardioExerciseList, other.cardioExerciseList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, strengthExerciseList, cardioExerciseList);
    }
}
