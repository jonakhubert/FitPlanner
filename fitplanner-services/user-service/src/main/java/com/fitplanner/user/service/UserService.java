package com.fitplanner.user.service;

import com.fitplanner.user.exception.model.UserNotFoundException;
import com.fitplanner.user.model.api.ChangePasswordRequest;
import com.fitplanner.user.model.api.ConfirmationResponse;
import com.fitplanner.user.model.api.UserDetailsRequest;
import com.fitplanner.user.model.user.NutritionInfo;
import com.fitplanner.user.model.user.User;
import com.fitplanner.user.model.user.UserDTO;
import com.fitplanner.user.model.user.UserNutrition;
import com.fitplanner.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    public ConfirmationResponse changePassword(ChangePasswordRequest request) {
        var user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setPassword(passwordEncoder.encode(request.password()));
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

    public ConfirmationResponse updateUserDetails(UserDetailsRequest request) {
        var user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new UserNotFoundException("User not found."));

        var currentNutritionInfo = user.getNutritionInfo();
        currentNutritionInfo.setFinishDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        user.getHistoricalNutritionInfos().add(currentNutritionInfo);

        setUserNutrients(user, request);
        userRepository.save(user);

        return new ConfirmationResponse("User updated successfully.");
    }

    public UserDTO findUserByEmail(String email) {
        return userRepository.findByEmail(email).map(userDTOMapper)
            .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public void saveUserNutrition(UserNutrition userNutrition) {
        var user = userRepository.findByEmail(userNutrition.email())
            .orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setDailyMealPlans(userNutrition.dailyMealPlans());
        userRepository.save(user);
    }

    private void setUserNutrients(User user, UserDetailsRequest request) {
        int baseCalories = request.activity_level() == 1 ? (int)(request.weight() * 31)
            : request.activity_level() == 2 ? (int)(request.weight() * 32)
            : (int)(request.weight() * 33);

        int totalCalories = request.goal() == 1 ? baseCalories - 300
            : request.goal() == 3 ? baseCalories + 300
            : baseCalories;

        double protein = request.weight() * 2;
        double fat = request.weight();
        double carbs = (totalCalories - (protein * 4) - (fat * 9)) / 4;

        var nutritionInfo = new NutritionInfo(totalCalories, protein, fat, carbs,request.height(),
            request.weight(), request.goal(), request.activity_level());

        user.setNutritionInfo(nutritionInfo);
    }
}
