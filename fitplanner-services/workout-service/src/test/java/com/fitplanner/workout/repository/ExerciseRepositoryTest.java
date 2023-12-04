package com.fitplanner.workout.repository;

import com.fitplanner.workout.MongoDBContainerConfig;
import com.fitplanner.workout.model.training.exercise.Exercise;
import com.fitplanner.workout.model.training.exercise.ExerciseType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@Testcontainers
@ContextConfiguration(classes = MongoDBContainerConfig.class)
public class ExerciseRepositoryTest {

    @Autowired
    private ExerciseRepository underTest;

    @Test
    public void findByNameAndExerciseTypeIgnoreCase_ExistingNameAndType_ExerciseList() {
        // given
        var name = "ex";
        var type = ExerciseType.STRENGTH;
        var exerciseList = new ArrayList<Exercise>();
        var e1 = new Exercise(name + "1", "link", "muscle", type);
        var e2 = new Exercise(name + "2", "link", "muscle", type);
        exerciseList.add(e1);
        exerciseList.add(e2);

        underTest.save(e1);
        underTest.save(e2);

        // when
        var result = underTest.findByNameAndExerciseTypeIgnoreCase(name, type).orElse(Collections.emptyList());

        // then
        assertEquals(result, exerciseList);
    }

    @Test
    public void findByNameAndExerciseTypeIgnoreCase_NonExistingNameAndType_ExerciseList() {
        // given
        var name = "name";
        var type = ExerciseType.STRENGTH;

        // when
        var result = underTest.findByNameAndExerciseTypeIgnoreCase(name, type).orElse(Collections.emptyList());

        // then
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void findByMuscleIgnoreCase_ExistingMuscle_ExerciseList() {
        // given
        var name = "test";
        var type = ExerciseType.STRENGTH;
        var muscle = "back";
        var exerciseList = new ArrayList<Exercise>();
        var e1 = new Exercise(name, "link", muscle, type);
        exerciseList.add(e1);

        underTest.save(e1);

        // when
        var result = underTest.findByMuscleIgnoreCase(muscle).orElse(Collections.emptyList());

        // then
        assertEquals(result, exerciseList);
    }
}
