package com.fitplanner.user.model.training;

import java.util.List;

public class WorkoutPlan {

    private String date;
    private List<ExerciseInfo> exerciseInfoList;

    public WorkoutPlan() {}

    public WorkoutPlan(String date, List<ExerciseInfo> exerciseInfoList) {
        this.date = date;
        this.exerciseInfoList = exerciseInfoList;
    }

    // getters
    public String getDate() { return date; }
    public List<ExerciseInfo> getExerciseInfoList() { return exerciseInfoList; }

    // setters
    public void setDate(String date) { this.date = date; }
    public void setExerciseInfoList(List<ExerciseInfo> exerciseInfoList) { this.exerciseInfoList = exerciseInfoList; }
}
