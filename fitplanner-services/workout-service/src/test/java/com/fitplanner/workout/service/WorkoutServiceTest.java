package com.fitplanner.workout.service;

import com.fitplanner.workout.client.UserServiceClient;
import com.fitplanner.workout.model.User;
import com.fitplanner.workout.model.api.ConfirmationResponse;
import com.fitplanner.workout.model.api.ExerciseRequest;
import com.fitplanner.workout.model.training.UserCardioExercise;
import com.fitplanner.workout.model.training.UserStrengthExercise;
import com.fitplanner.workout.model.training.WorkoutPlan;
import com.fitplanner.workout.model.training.exercise.Exercise;
import com.fitplanner.workout.model.training.exercise.ExerciseType;
import com.fitplanner.workout.repository.ExerciseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private WorkoutService underTest;

    @Test
    public void addUserStrengthExercise_ValidParameters_ConfirmationResponse() {
        // given
        var email = "any@gmail.com";
        var date = "2023-10-10";
        var strengthExercise = new UserStrengthExercise("ex", "link", 4, 10, 10);
        var header = "Bearer token";
        var workoutPlanList = new ArrayList<WorkoutPlan>();
        var user = new User(email, workoutPlanList);
        var expected = new ConfirmationResponse("Exercise has been added.");

        when(userServiceClient.getUser(email, header)).thenReturn(user);

        // when
        var result = underTest.addUserStrengthExercise(email, date, strengthExercise, header);

        // then
        assertEquals(expected, result);
        verify(userServiceClient, times(1)).getUser(eq(email), eq(header));
        verify(userServiceClient, times(1)).saveUserWorkoutPlanList(eq(email), eq(workoutPlanList), eq(header));
    }

    @Test
    public void removeUserStrengthExercise_ValidParameters_ConfirmationResponse() {
        // given
        var email = "any@gmail.com";
        var date = "2023-10-10";
        var strengthExerciseId = "id";
        var header = "Bearer token";
        var workoutPlanList = new ArrayList<WorkoutPlan>();
        var user = new User(email, workoutPlanList);
        var expected = new ConfirmationResponse("Exercise has been removed.");

        when(userServiceClient.getUser(email, header)).thenReturn(user);

        // when
        var result = underTest.removeUserStrengthExercise(email, date, strengthExerciseId, header);

        // then
        assertEquals(expected, result);
        verify(userServiceClient, times(1)).getUser(eq(email), eq(header));
        verify(userServiceClient, times(1)).saveUserWorkoutPlanList(eq(email), eq(workoutPlanList), eq(header));
    }

    @Test
    public void addUserCardioExercise_ValidParameters_ConfirmationResponse() {
        // given
        var email = "any@gmail.com";
        var date = "2023-10-10";
        var cardioExercise = new UserCardioExercise("ex", 10);
        var header = "Bearer token";
        var workoutPlanList = new ArrayList<WorkoutPlan>();
        var user = new User(email, workoutPlanList);
        var expected = new ConfirmationResponse("Exercise has been added.");

        when(userServiceClient.getUser(email, header)).thenReturn(user);

        // when
        var result = underTest.addUserCardioExercise(email, date, cardioExercise, header);

        // then
        assertEquals(expected, result);
        verify(userServiceClient, times(1)).getUser(eq(email), eq(header));
        verify(userServiceClient, times(1)).saveUserWorkoutPlanList(eq(email), eq(workoutPlanList), eq(header));
    }

    @Test
    public void removeUserCardioExercise_ValidParameters_ConfirmationResponse() {
        // given
        var email = "any@gmail.com";
        var date = "2023-10-10";
        var cardioExerciseId = "id";
        var header = "Bearer token";
        var workoutPlanList = new ArrayList<WorkoutPlan>();
        var user = new User(email, workoutPlanList);
        var expected = new ConfirmationResponse("Exercise has been removed.");

        when(userServiceClient.getUser(email, header)).thenReturn(user);

        // when
        var result = underTest.removeUserStrengthExercise(email, date, cardioExerciseId, header);

        // then
        assertEquals(expected, result);
        verify(userServiceClient, times(1)).getUser(eq(email), eq(header));
        verify(userServiceClient, times(1)).saveUserWorkoutPlanList(eq(email), eq(workoutPlanList), eq(header));
    }

    @Test
    public void getWorkoutPlan_ValidParameters_WorkoutPlan() {
        // given
        var email = "any@gmail.com";
        var date = "2023-10-10";
        var header = "Bearer token";
        var workoutPlanList = new ArrayList<WorkoutPlan>();
        var workoutPlan = new WorkoutPlan(date, new ArrayList<>(), new ArrayList<>());
        workoutPlanList.add(workoutPlan);
        var user = new User(email, workoutPlanList);

        when(userServiceClient.getUser(email, header)).thenReturn(user);

        // when
        var result = underTest.getWorkoutPlan(email, date, header);

        // then
        assertEquals(workoutPlan, result);
        verify(userServiceClient, times(1)).getUser(eq(email), eq(header));
    }

    @Test
    public void getExercisesByName_ValidExerciseNameAndType_ExerciseList() {
        // given
        var name = "ex";
        var type = ExerciseType.STRENGTH;
        var exercise = new Exercise(name, "link", "muscle", type);
        var exerciseList = new ArrayList<Exercise>();
        exerciseList.add(exercise);

        when(exerciseRepository.findByNameAndExerciseTypeIgnoreCase(name, type)).thenReturn(Optional.of(exerciseList));

        // when
        var result = underTest.getExercisesByName(name, type);

        // then
        assertEquals(result, exerciseList);
        verify(exerciseRepository, times(1)).findByNameAndExerciseTypeIgnoreCase(eq(name), eq(type));
    }

    @Test
    public void getExercisesByMuscle_ValidMuscle_ExerciseList() {
        // given
        var name = "ex";
        var type = ExerciseType.STRENGTH;
        var muscle = "muscle";
        var exercise = new Exercise(name, "link", muscle, type);
        var exerciseList = new ArrayList<Exercise>();
        exerciseList.add(exercise);

        when(exerciseRepository.findByMuscleIgnoreCase(muscle)).thenReturn(Optional.of(exerciseList));

        // when
        var result = underTest.getExercisesByMuscle(muscle);

        // then
        assertEquals(result, exerciseList);
        verify(exerciseRepository, times(1)).findByMuscleIgnoreCase(eq(muscle));
    }

    @Test
    public void addExercise_ExerciseRequest_SavedExercise() {
        // given
        var request = new ExerciseRequest("ex", "link", "muscle", ExerciseType.STRENGTH);

        // when
        underTest.addExercise(request);

        // then
        verify(exerciseRepository, times(1)).save(any(Exercise.class));
    }
}
