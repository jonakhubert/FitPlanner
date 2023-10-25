package com.fitplanner.user.model.training;

import java.util.List;

public class WorkoutPlan {

    private String date;
    private List<ExerciseInfo> exerciseInfos;

    public WorkoutPlan() {}

    // getters
    public String getDate() { return date; }
    public List<ExerciseInfo> getExerciseList() { return exerciseInfos; }
}
