package com.fitplanner.authentication.service;

import com.fitplanner.authentication.exception.model.UserNotFoundException;
import com.fitplanner.authentication.model.tokens.ResetPasswordToken;
import com.fitplanner.authentication.model.tokens.VerificationToken;
import com.fitplanner.authentication.model.tokens.accesstoken.AccessToken;
import com.fitplanner.authentication.model.api.RegisterRequest;
import com.fitplanner.authentication.model.user.NutritionInfo;
import com.fitplanner.authentication.model.user.Role;
import com.fitplanner.authentication.model.user.User;
import com.fitplanner.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public User getUserByVerificationToken(String token) {
        return userRepository.findByVerificationToken(token)
            .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public User createUser(RegisterRequest request) {
        var user = new User(
            request.firstName(),
            request.lastName(),
            request.email(),
            passwordEncoder.encode(request.password()),
            Role.USER
        );

        setUserNutrients(user, request);

        return userRepository.save(user);
    }

    public void verifyUser(User user) {
        user.getVerificationToken().setConfirmedAt(LocalDateTime.now());
        user.setEnabled(true);

        userRepository.save(user);
    }

    public boolean isUserExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void resetPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setAccessToken(null);

        userRepository.save(user);
    }

    public AccessToken getAccessToken(String token) {
        return userRepository.findByAccessToken(token)
            .map(User::getAccessToken)
            .orElse(null);
    }

    public AccessToken createAccessToken(User user, String token) {
        var accessToken = new AccessToken(token);

        user.setAccessToken(accessToken);
        userRepository.save(user);

        return accessToken;
    }

    public void revokeAccessToken(String token) {
        var user = userRepository.findByAccessToken(token)
            .orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setAccessToken(null);
        userRepository.save(user);
    }

    public boolean isAccessTokenValid(String token) {
        return userRepository.findByAccessToken(token).isPresent();
    }

    public VerificationToken createVerificationToken(User user) {
        var verificationToken = new VerificationToken(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15)
        );

        user.setVerificationToken(verificationToken);
        userRepository.save(user);

        return verificationToken;
    }

    public VerificationToken getVerificationToken(String token) {
        return userRepository.findByVerificationToken(token)
            .map(User::getVerificationToken)
            .orElse(null);
    }

    public ResetPasswordToken createResetPasswordToken(User user) {
        var resetPasswordToken = new ResetPasswordToken(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15)
        );

        user.setResetPasswordToken(resetPasswordToken);
        userRepository.save(user);

        return resetPasswordToken;
    }

    public boolean isResetPasswordTokenValid(String token) {
        return userRepository.findByResetPasswordToken(token).isPresent();
    }

    private void setUserNutrients(User user, RegisterRequest request) {
        int baseCalories = request.activity_level() == 1 ? (int)(request.weight() * 31)
            : request.activity_level() == 2 ? (int)(request.weight() * 32)
            : (int)(request.weight() * 33);

        int totalCalories = request.goal() == 1 ? baseCalories - 300
            : request.goal() == 3 ? baseCalories + 300
            : baseCalories;

        double protein = request.weight() * 2;
        double fat = request.weight();
        double carbs = (totalCalories - (protein * 4) - (fat * 9)) / 4;

        var nutritionInfo = new NutritionInfo(totalCalories, protein, fat, carbs, request.height(), request.weight(),
            request.goal(), request.activity_level());

        user.setNutritionInfo(nutritionInfo);
    }
}
