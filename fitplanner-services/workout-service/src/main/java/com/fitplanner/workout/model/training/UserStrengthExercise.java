package com.fitplanner.workout.model.training;

import java.util.Objects;
import java.util.UUID;

public class UserStrengthExercise {

    private String id;
    private String name;
    private String link;
    private int sets;
    private int reps;
    private double weight;

    public UserStrengthExercise() {
        this.id = UUID.randomUUID().toString();
    }

    public UserStrengthExercise(String name, String link, int sets, int reps, double weight) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.link = link;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    // getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getLink() { return link; }
    public int getSets() { return sets; }
    public int getReps() { return reps; }
    public double getWeight() { return weight; }

    // setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setLink(String link) { this.link = link; }
    public void setSets(int sets) { this.sets = sets; }
    public void setReps(int reps) { this.reps = reps; }
    public void setWeight(double weight) { this.weight = weight; }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof UserStrengthExercise other))
            return false;

        return  Objects.equals(id, other.id) &&
                Objects.equals(name, other.name) &&
                Objects.equals(link, other.link) &&
                Objects.equals(sets, other.sets) &&
                Objects.equals(reps, other.reps) &&
                Objects.equals(weight, other.weight);
    }

    @Override
    public int hashCode() { return Objects.hash(id, name, link, sets, reps, weight); }
}
