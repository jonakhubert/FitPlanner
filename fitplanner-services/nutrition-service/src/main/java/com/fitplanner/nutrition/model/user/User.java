package com.fitplanner.nutrition.model.user;

import com.fitplanner.nutrition.model.food.DailyMealPlan;
import com.fitplanner.nutrition.model.tokens.ResetPasswordToken;
import com.fitplanner.nutrition.model.tokens.VerificationToken;
import com.fitplanner.nutrition.model.tokens.accesstoken.AccessToken;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private List<DailyMealPlan> dailyMealPlans;

    // getters
    public List<DailyMealPlan> getDailyMealPlans() { return dailyMealPlans; }

    // setters
    public void setCalories(double calories) { this.calories = calories; }
    public void setDailyMealPlans(List<DailyMealPlan> dailyMealPlans) { this.dailyMealPlans = dailyMealPlans; }
}
