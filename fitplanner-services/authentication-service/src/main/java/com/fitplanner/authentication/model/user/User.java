package com.fitplanner.authentication.model.user;

import com.fitplanner.authentication.model.food.MealPlan;
import com.fitplanner.authentication.model.tokens.ResetPasswordToken;
import com.fitplanner.authentication.model.tokens.VerificationToken;
import com.fitplanner.authentication.model.tokens.accesstoken.AccessToken;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Document(value = "users")
public class User implements UserDetails {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean enabled = false;

    // tokens
    private AccessToken accessToken;
    private VerificationToken verificationToken;
    private ResetPasswordToken resetPasswordToken;

    // nutrition
    private NutritionInfo nutritionInfo;
    private List<NutritionInfo> historicalNutritionInfos;
    private List<MealPlan> mealPlans;

    public User(String firstName, String lastName, String email, String password, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.accessToken = null;
        this.verificationToken = null;
        this.resetPasswordToken = null;
        this.historicalNutritionInfos = new ArrayList<>();
        this.mealPlans = new ArrayList<>();
    }

    public User() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // getters
    @Override public String getUsername() { return email; }
    @Override public String getPassword() { return password; }
    @Override  public boolean isEnabled() { return enabled; }
    public AccessToken getAccessToken() { return accessToken; }
    public VerificationToken getVerificationToken() { return verificationToken; }
    public ResetPasswordToken getResetPasswordToken() { return resetPasswordToken; }

    // setters
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public void setPassword(String password) { this.password = password; }
    public void setAccessToken(AccessToken accessToken) { this.accessToken = accessToken; }
    public void setVerificationToken(VerificationToken verificationToken) { this.verificationToken = verificationToken; }
    public void setResetPasswordToken(ResetPasswordToken resetPasswordToken) { this.resetPasswordToken = resetPasswordToken; }
    public void setNutritionInfo(NutritionInfo nutritionInfo) { this.nutritionInfo = nutritionInfo; }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof User other))
            return false;

        return  Objects.equals(id, other.id) &&
                Objects.equals(firstName, other.firstName) &&
                Objects.equals(lastName, other.lastName) &&
                Objects.equals(email, other.email) &&
                Objects.equals(password, other.password) &&
                Objects.equals(role, other.role) &&
                Objects.equals(enabled, other.enabled) &&
                Objects.equals(nutritionInfo, other.nutritionInfo) &&
                Objects.equals(accessToken, other.accessToken) &&
                Objects.equals(verificationToken, other.verificationToken) &&
                Objects.equals(resetPasswordToken, other.resetPasswordToken) &&
                Objects.equals(historicalNutritionInfos, other.historicalNutritionInfos) &&
                Objects.equals(mealPlans, other.mealPlans);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, role, enabled, accessToken, verificationToken,
            resetPasswordToken, nutritionInfo, historicalNutritionInfos, mealPlans);
    }
}
