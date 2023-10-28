package com.fitplanner.workout.model.training;

import java.util.ArrayList;
import java.util.List;

public class WorkoutPlan {

    private String date;
    private List<ExerciseInfo> exerciseInfoList;

    public WorkoutPlan(String date) {
        this.date = date;
        this.exerciseInfoList = new ArrayList<>();
    }

    public WorkoutPlan(String date, List<ExerciseInfo> exerciseInfoList) {
        this.date = date;
        this.exerciseInfoList = exerciseInfoList;
    }

    public WorkoutPlan() {}

    // getters
    public String getDate() { return date; }
    public List<ExerciseInfo> getExerciseInfoList() { return exerciseInfoList; }

    // setters
    public void setDate(String date) { this.date = date; }
    public void setExerciseInfoList(List<ExerciseInfo> exerciseInfoList) { this.exerciseInfoList = exerciseInfoList; }
}
