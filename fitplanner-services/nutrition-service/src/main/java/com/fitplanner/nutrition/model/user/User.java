package com.fitplanner.nutrition.model.user;

import com.fitplanner.nutrition.model.DailyMealPlan;
import com.fitplanner.nutrition.model.tokens.ResetPasswordToken;
import com.fitplanner.nutrition.model.tokens.VerificationToken;
import com.fitplanner.nutrition.model.tokens.accesstoken.AccessToken;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(value = "users")
public class User {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean enabled;

    // tokens
    private AccessToken accessToken;
    private VerificationToken verificationToken;
    private ResetPasswordToken resetPasswordToken;

    // preferences TODO: add other parameters (height, weight, etc.)
    private double calories;

    @DocumentReference
    private List<DailyMealPlan> dailyMealPlans;

    public User(
        String firstName, String lastName, String email, String password, Role role, Boolean enabled,
        List<DailyMealPlan> dailyMealPlans
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
        this.dailyMealPlans = dailyMealPlans;
    }

    public User() {}

    // getters
    public List<DailyMealPlan> getDailyMealPlans() { return dailyMealPlans; }

    // setters
    public void setCalories(double calories) { this.calories = calories; }
}
