package com.fitplanner.workout.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitplanner.workout.model.api.ConfirmationResponse;
import com.fitplanner.workout.model.api.ExerciseRequest;
import com.fitplanner.workout.model.training.UserCardioExercise;
import com.fitplanner.workout.model.training.UserStrengthExercise;
import com.fitplanner.workout.model.training.WorkoutPlan;
import com.fitplanner.workout.model.training.exercise.Exercise;
import com.fitplanner.workout.model.training.exercise.ExerciseType;
import com.fitplanner.workout.service.WorkoutService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkoutController.class)
public class WorkoutControllerTest {

    @MockBean
    private WorkoutService workoutService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = "/api/workout";

    @Test
    public void addUserStrengthExercise_ValidParameters_ConfirmationResponse() throws Exception {
        // given
        var email = "any@gmail.com";
        var date = "2023-10-10";
        var strengthExercise = new UserStrengthExercise("ex", "link", 4, 10, 10);
        var header = "Bearer token";
        var response = new ConfirmationResponse("Exercise has been added.");

        when(workoutService.addUserStrengthExercise(eq(email), eq(date), eq(strengthExercise), eq(header)))
            .thenReturn(response);

        // then
        mockMvc.perform(post(baseUrl + "/users/{email}/workout-plans/{date}/strength-exercises", email, date)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", header)
            .content(objectMapper.writeValueAsString(strengthExercise)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.confirmation_message").value(response.message()));
    }

    @Test
    public void removeUserStrengthExercise_ValidParameters_ConfirmationResponse() throws Exception {
        // given
        var email = "any@gmail.com";
        var date = "2023-10-10";
        var strengthExerciseId = "id";
        var header = "Bearer token";
        var response = new ConfirmationResponse("Exercise has been removed.");

        when(workoutService.removeUserStrengthExercise(eq(email), eq(date), eq(strengthExerciseId), eq(header)))
                .thenReturn(response);

        // then
        mockMvc.perform(delete(baseUrl + "/users/{email}/workout-plans/{date}/strength-exercises/{id}", email, date,
            strengthExerciseId)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", header))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.confirmation_message").value(response.message()));
    }

    @Test
    public void addUserCardioExercise_ValidParameters_ConfirmationResponse() throws Exception {
        // given
        var email = "any@gmail.com";
        var date = "2023-10-10";
        var cardioExercise = new UserCardioExercise("ex", 10);
        var header = "Bearer token";
        var response = new ConfirmationResponse("Exercise has been added.");

        when(workoutService.addUserCardioExercise(eq(email), eq(date), eq(cardioExercise), eq(header)))
                .thenReturn(response);

        // then
        mockMvc.perform(post(baseUrl + "/users/{email}/workout-plans/{date}/cardio-exercises", email, date)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", header)
            .content(objectMapper.writeValueAsString(cardioExercise)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.confirmation_message").value(response.message()));
    }

    @Test
    public void removeUserCardioExercise_ValidParameters_ConfirmationResponse() throws Exception {
        // given
        var email = "any@gmail.com";
        var date = "2023-10-10";
        var cardioExerciseId = "id";
        var header = "Bearer token";
        var response = new ConfirmationResponse("Exercise has been removed.");

        when(workoutService.removeUserCardioExercise(eq(email), eq(date), eq(cardioExerciseId), eq(header)))
            .thenReturn(response);

        // then
        mockMvc.perform(delete(baseUrl + "/users/{email}/workout-plans/{date}/cardio-exercises/{id}", email, date,
            cardioExerciseId)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", header))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.confirmation_message").value(response.message()));
    }

    @Test
    public void getUserWorkoutPlan_ValidParameters_WorkoutPlan() throws Exception {
        // given
        var email = "any@gmail.com";
        var date = "2023-10-10";
        var header = "Bearer token";
        var workoutPlan = new WorkoutPlan(date);

        when(workoutService.getWorkoutPlan(eq(email), eq(date), eq(header))).thenReturn(workoutPlan);

        // then
        mockMvc.perform(get(baseUrl + "/users/{email}/workout-plans/{date}", email, date)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", header))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.date").value(date));
    }

    @Test
    public void getExercisesByName_ValidParameters_ExerciseList() throws Exception {
        // given
        var name = "ex";
        var link = "link";
        var muscle = "muscle";
        var type = ExerciseType.STRENGTH;
        var exercise = new Exercise(name, link, muscle, type);

        when(workoutService.getExercisesByName(eq(name), eq(type))).thenReturn(List.of(exercise));

        // then
        mockMvc.perform(get(baseUrl + "/exercises/by-name")
            .param("name", name)
            .param("type", String.valueOf(type))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value(exercise.getName()))
            .andExpect(jsonPath("$[0].link").value(exercise.getLink()))
            .andExpect(jsonPath("$[0].muscle").value(exercise.getMuscle()))
            .andExpect(jsonPath("$[0].exerciseType").value(exercise.getExerciseType().toString()));
    }

    @Test
    public void getExercisesByMuscle_ValidParameters_ExerciseList() throws Exception {
        // given
        var name = "ex";
        var link = "link";
        var muscle = "muscle";
        var type = ExerciseType.STRENGTH;
        var exercise = new Exercise(name, link, muscle, type);

        when(workoutService.getExercisesByMuscle(eq(muscle))).thenReturn(List.of(exercise));

        // then
        mockMvc.perform(get(baseUrl + "/exercises/by-muscle")
            .param("muscle", muscle)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value(exercise.getName()))
            .andExpect(jsonPath("$[0].link").value(exercise.getLink()))
            .andExpect(jsonPath("$[0].muscle").value(exercise.getMuscle()))
            .andExpect(jsonPath("$[0].exerciseType").value(exercise.getExerciseType().toString()));
    }

    @Test
    public void addExercise_ExerciseRequest_ExerciseSave() throws Exception {
        // given
        var name = "ex";
        var link = "link";
        var muscle = "muscle";
        var type = ExerciseType.STRENGTH;
        var request = new ExerciseRequest(name, link, muscle, type);

        // then
        mockMvc.perform(post(baseUrl + "/exercises")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }
}
