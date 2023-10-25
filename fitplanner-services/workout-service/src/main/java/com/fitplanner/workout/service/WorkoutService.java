package com.fitplanner.workout.service;

import com.fitplanner.workout.model.api.ExerciseRequest;
import com.fitplanner.workout.model.training.Exercise;
import com.fitplanner.workout.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class WorkoutService {

    private final ExerciseRepository exerciseRepository;

    @Autowired
    public WorkoutService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public void addExercise(ExerciseRequest exerciseRequest) {
        var exercise = new Exercise(exerciseRequest.name(), exerciseRequest.link());
        exerciseRepository.save(exercise);
    }

    public List<Exercise> getExercises(String name) {
        if(name.isEmpty())
            return Collections.emptyList();

        return exerciseRepository.findByNameIgnoreCase(name)
            .orElse(Collections.emptyList());
    }
}
