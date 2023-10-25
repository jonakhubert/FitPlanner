package com.fitplanner.workout.model.training;

import java.util.Objects;
import java.util.UUID;

public class ExerciseInfo {

    private String id;
    private String name;
    private String link;
    private Integer sets;
    private Integer reps;
    private Double weight;

    public ExerciseInfo() {
        this.id = UUID.randomUUID().toString();
    }

    // getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getLink() { return link; }
    public Integer getSets() { return sets; }
    public Integer getReps() { return reps; }
    public Double getWeight() { return weight; }

    // setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setLink(String link) { this.link = link; }
    public void setSets(Integer sets) { this.sets = sets; }
    public void setReps(Integer reps) { this.reps = reps; }
    public void setWeight(Double weight) { this.weight = weight; }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof ExerciseInfo other))
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
