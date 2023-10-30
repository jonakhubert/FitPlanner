package com.fitplanner.workout.repository;

import com.fitplanner.workout.model.training.CardioExercise;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardioExerciseRepository extends MongoRepository<CardioExercise, String> {

    @Query("{'name': {$regex : ?0, $options: 'i'}}")
    Optional<List<CardioExercise>> findByNameIgnoreCase(String name);
}
