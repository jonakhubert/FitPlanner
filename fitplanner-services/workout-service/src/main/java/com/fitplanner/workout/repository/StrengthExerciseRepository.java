package com.fitplanner.workout.repository;

import com.fitplanner.workout.model.training.StrengthExercise;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StrengthExerciseRepository extends MongoRepository<StrengthExercise, String> {

    @Query("{'name': {$regex : ?0, $options: 'i'}}")
    Optional<List<StrengthExercise>> findByNameIgnoreCase(String name);
}
