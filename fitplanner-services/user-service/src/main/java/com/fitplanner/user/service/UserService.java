package com.fitplanner.user.service;

import com.fitplanner.user.exception.model.UserNotFoundException;
import com.fitplanner.user.model.api.ConfirmationResponse;
import com.fitplanner.user.model.api.UserDetailsRequest;
import com.fitplanner.user.model.food.MealPlan;
import com.fitplanner.user.model.training.WorkoutPlan;
import com.fitplanner.user.model.user.NutritionInfo;
import com.fitplanner.user.model.user.User;
import com.fitplanner.user.model.user.UserDTO;
import com.fitplanner.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserDTOMapper userDTOMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userDTOMapper = userDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public ConfirmationResponse changePassword(String email, String newPassword) {
        var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setAccessToken(null);
        userRepository.save(user);

        return new ConfirmationResponse("Password has been changed.");
    }

    public ConfirmationResponse deleteAccount(String email) {
        var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found."));

        userRepository.delete(user);

        return new ConfirmationResponse("Account has been deleted.");
    }

    public ConfirmationResponse updateUserDetails(String email, UserDetailsRequest request) {
        var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found."));

        var newDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        var currentNutritionInfo = user.getNutritionInfo();

        if(!newDate.equals(currentNutritionInfo.getBeginDate())) {
            currentNutritionInfo.setFinishDate(newDate);
            user.getHistoricalNutritionInfoList().add(currentNutritionInfo);

            setUserNutrients(user, request);
        }

        setUserNutrients(user, request);
        userRepository.save(user);

        return new ConfirmationResponse("User updated successfully.");
    }

    public UserDTO findUserByEmail(String email) {
        return userRepository.findByEmail(email).map(userDTOMapper)
            .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public void saveUserMealPlanList(String email, List<MealPlan> mealPlanList) {
        var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setMealPlanList(mealPlanList);
        userRepository.save(user);
    }

    public void saveUserWorkoutPlanList(String email, List<WorkoutPlan> workoutPlanList) {
        var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setWorkoutPlanList(workoutPlanList);
        userRepository.save(user);
    }

    private void setUserNutrients(User user, UserDetailsRequest request) {
        var baseCalories = request.activity_level() == 1 ? (int)(request.weight() * 31)
            : request.activity_level() == 2 ? (int)(request.weight() * 32)
            : (int)(request.weight() * 33);

        var totalCalories = request.goal() == 1 ? baseCalories - 300
            : request.goal() == 3 ? baseCalories + 300
            : baseCalories;

        var protein = (int) request.weight() * 2;
        var fat = (int) request.weight();
        var carbs = (totalCalories - (protein * 4) - (fat * 9)) / 4;

        var nutritionInfo = new NutritionInfo(totalCalories, protein, fat, carbs,request.height(), request.weight(),
            request.goal(), request.activity_level());

        user.setNutritionInfo(nutritionInfo);
    }
}
