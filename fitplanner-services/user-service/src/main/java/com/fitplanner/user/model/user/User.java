package com.fitplanner.user.model.user;

import com.fitplanner.user.model.food.MealPlan;
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

    private NutritionInfo nutritionInfo;
    private List<NutritionInfo> historicalNutritionInfos;
    private List<MealPlan> mealPlans;

    // getters
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public NutritionInfo getNutritionInfo() { return nutritionInfo; }
    public List<NutritionInfo> getHistoricalNutritionInfos() { return historicalNutritionInfos; }
    public List<MealPlan> getMealPlans() { return mealPlans; }

    // setters
    public void setPassword(String password) { this.password = password; }
    public void setAccessToken(AccessToken accessToken) { this.accessToken = accessToken; }
    public void setDailyMealPlans(List<MealPlan> mealPlans) { this.mealPlans = mealPlans; }
    public void setNutritionInfo(NutritionInfo nutritionInfo) { this.nutritionInfo = nutritionInfo; }
}
