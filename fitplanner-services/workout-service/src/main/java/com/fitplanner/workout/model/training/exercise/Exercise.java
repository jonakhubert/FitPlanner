package com.fitplanner.workout.model.training.exercise;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "exercises")
public class Exercise {

    @Id
    private String id;
    private String name;
    private String link;
    private String muscle;
    private ExerciseType exerciseType;

    public Exercise(String name, String link, String muscle, ExerciseType exerciseType) {
        this.name = name;
        this.link = link;
        this.muscle = muscle;
        this.exerciseType = exerciseType;
    }

    // getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getLink() { return link; }
    public String getMuscle() { return muscle; }
    public ExerciseType getExerciseType() { return exerciseType; }
}
