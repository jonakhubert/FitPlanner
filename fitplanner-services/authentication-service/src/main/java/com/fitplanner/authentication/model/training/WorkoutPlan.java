package com.fitplanner.authentication.model.training;

import java.util.List;

public class WorkoutPlan {

    private String date;
    private List<ExerciseInfo> exerciseInfoList;

    public WorkoutPlan() {}

    // getters
    public String getDate() { return date; }
    public List<ExerciseInfo> getExerciseInfoList() { return exerciseInfoList; }
}
