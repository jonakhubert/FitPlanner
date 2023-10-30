package com.fitplanner.workout.repository;

import com.fitplanner.workout.model.training.exercise.Exercise;
import com.fitplanner.workout.model.training.exercise.ExerciseType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends MongoRepository<Exercise, String> {

    @Query("{'name': {$regex : ?0, $options: 'i'}, 'exerciseType': ?1}")
    Optional<List<Exercise>> findByNameAndExerciseTypeIgnoreCase(String name, ExerciseType exerciseType);
}
