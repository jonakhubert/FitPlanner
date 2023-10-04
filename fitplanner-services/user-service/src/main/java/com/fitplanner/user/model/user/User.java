package com.fitplanner.user.model.user;

import com.fitplanner.user.model.food.DailyMealPlan;
import com.fitplanner.user.model.tokens.ResetPasswordToken;
import com.fitplanner.user.model.tokens.VerificationToken;
import com.fitplanner.user.model.tokens.accesstoken.AccessToken;
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
    public String getEmail() { return email; }
    public double getCalories() { return calories; }
    public List<DailyMealPlan> getDailyMealPlans() { return dailyMealPlans; }

    // setters
    public void setPassword(String password) { this.password = password; }
    public void setDailyMealPlans(List<DailyMealPlan> dailyMealPlans) { this.dailyMealPlans = dailyMealPlans; }
}
