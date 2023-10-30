package com.fitplanner.workout.model.training.exercise;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "exercises")
public class Exercise {

    @Id
    private String id;
    private String name;
    private String link;
    private ExerciseType exerciseType;

    public Exercise(String name, String link, ExerciseType exerciseType) {
        this.name = name;
        this.link = link;
        this.exerciseType = exerciseType;
    }

    // getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getLink() { return link; }
    public ExerciseType getExerciseType() { return exerciseType; }
}
