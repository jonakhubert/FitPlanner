package com.fitplanner.workout.model.training;

import java.util.ArrayList;
import java.util.List;

public class WorkoutPlan {

    private String date;
    private List<ExerciseInfo> exerciseInfos;

    public WorkoutPlan(String date) {
        this.date = date;
        this.exerciseInfos = new ArrayList<>();
    }

    public WorkoutPlan(String date, List<ExerciseInfo> exerciseInfos) {
        this.date = date;
        this.exerciseInfos = exerciseInfos;
    }

    public WorkoutPlan() {}

    // getters
    public String getDate() { return date; }
    public List<ExerciseInfo> getExerciseInfos() { return exerciseInfos; }

    // setters
    public void setDate(String date) { this.date = date; }
    public void setExerciseInfos(List<ExerciseInfo> exerciseInfos) { this.exerciseInfos = exerciseInfos; }
}
