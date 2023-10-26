package com.fitplanner.user.model.training;

import java.util.List;

public class WorkoutPlan {

    private String date;
    private List<ExerciseInfo> exerciseInfos;

    public WorkoutPlan() {}

    public WorkoutPlan(String date, List<ExerciseInfo> exerciseInfos) {
        this.date = date;
        this.exerciseInfos = exerciseInfos;
    }

    // getters
    public String getDate() { return date; }
    public List<ExerciseInfo> getExerciseInfos() { return exerciseInfos; }

    // setters
    public void setDate(String date) { this.date = date; }
    public void setExerciseInfos(List<ExerciseInfo> exerciseInfos) { this.exerciseInfos = exerciseInfos; }
}
