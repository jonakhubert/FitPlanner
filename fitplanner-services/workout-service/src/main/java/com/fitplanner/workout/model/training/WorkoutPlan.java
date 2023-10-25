package com.fitplanner.workout.model.training;

import java.util.List;

public class WorkoutPlan {

    private String date;
    private List<ExerciseInfo> exerciseInfos;

    public WorkoutPlan(String date, List<ExerciseInfo> exerciseInfos) {
        this.date = date;
        this.exerciseInfos = exerciseInfos;
    }

    // getters
    public String getDate() { return date; }
    public List<ExerciseInfo> getExerciseList() { return exerciseInfos; }
}
