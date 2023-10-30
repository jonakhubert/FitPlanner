package com.fitplanner.workout.model.training;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "cardio_exercises")
public class CardioExercise {

    @Id
    private String id;
    private String name;

    public CardioExercise(String name) {
        this.name = name;
    }

    // getters
    public String getId() { return id; }
    public String getName() { return name; }
}
