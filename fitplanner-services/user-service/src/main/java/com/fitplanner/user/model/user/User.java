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

    private int calories;
    private double protein;
    private double fat;
    private double carbs;
    private double height;
    private double weight;
    private int goal;
    private int activity_level;
    private List<DailyMealPlan> dailyMealPlans;

    // getters
    public String getEmail() { return email; }
    public int getCalories() { return calories; }
    public double getProtein() { return protein; }
    public double getFat() { return fat; }
    public double getCarbs() { return carbs; }
    public List<DailyMealPlan> getDailyMealPlans() { return dailyMealPlans; }

    // setters
    public void setPassword(String password) { this.password = password; }
    public void setAccessToken(AccessToken accessToken) { this.accessToken = accessToken; }
    public void setDailyMealPlans(List<DailyMealPlan> dailyMealPlans) { this.dailyMealPlans = dailyMealPlans; }
}
