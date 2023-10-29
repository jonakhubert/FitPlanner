package com.fitplanner.workout.model.training;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "strength_exercises")
public class StrengthExercise {

    @Id
    private String id;
    private String name;
    private String link;

    public StrengthExercise(String name, String link) {
        this.name = name;
        this.link = link;
    }

    // getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getLink() { return link; }
}
