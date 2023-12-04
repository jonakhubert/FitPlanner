package com.fitplanner.workout.model.training.exercise;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(value = "exercises")
public class Exercise {

    @Id
    private String id;
    private String name;
    private String link;
    private String muscle;
    private ExerciseType exerciseType;

    public Exercise() {}

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

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof Exercise other))
            return false;

        return  Objects.equals(id, other.id) &&
                Objects.equals(name, other.name) &&
                Objects.equals(link, other.link) &&
                Objects.equals(muscle, other.muscle) &&
                Objects.equals(exerciseType, other.exerciseType);
    }

    @Override
    public int hashCode() { return Objects.hash(id, name, link, muscle, exerciseType); }
}
