package com.fitplanner.workout.model.training;

import java.util.Objects;
import java.util.UUID;

public class UserCardioExercise {

    public String id;
    public String name;
    public double minutes;

    public UserCardioExercise() {
        this.id = UUID.randomUUID().toString();
    }

    public UserCardioExercise(String name, double minutes) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.minutes = minutes;
    }

    // getters
    public String getId() { return id; }
    public String getName() { return name; }
    public double getMinutes() { return minutes; }

    // setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setMinutes(double minutes) { this.minutes = minutes; }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof UserCardioExercise other))
            return false;

        return  Objects.equals(id, other.id) &&
                Objects.equals(name, other.name) &&
                Objects.equals(minutes, other.minutes);
    }

    @Override
    public int hashCode() { return Objects.hash(id, name, minutes); }
}
