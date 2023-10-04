package com.fitplanner.authentication.model.user;

import com.fitplanner.authentication.model.food.DailyMealPlan;
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

    private double calories;
    private double height;
    private double weight;
    private int goal;
    private List<DailyMealPlan> dailyMealPlans;

    public User(
        String firstName,
        String lastName,
        String email,
        String password,
        Role role,
        double height,
        double weight,
        int goal
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.height = height;
        this.weight = weight;
        this.goal = goal;
        this.accessToken = null;
        this.verificationToken = null;
        this.resetPasswordToken = null;
        this.dailyMealPlans = new ArrayList<>();
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
    public void setCalories(double calories) { this.calories = calories; }

    // setters
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public void setPassword(String password) { this.password = password; }
    public void setAccessToken(AccessToken accessToken) { this.accessToken = accessToken; }
    public void setVerificationToken(VerificationToken verificationToken) { this.verificationToken = verificationToken; }
    public void setResetPasswordToken(ResetPasswordToken resetPasswordToken) { this.resetPasswordToken = resetPasswordToken; }

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
                Objects.equals(calories, other.calories) &&
                Objects.equals(height, other.height) &&
                Objects.equals(weight, other.weight) &&
                Objects.equals(goal, other.goal) &&
                Objects.equals(accessToken, other.accessToken) &&
                Objects.equals(verificationToken, other.verificationToken) &&
                Objects.equals(resetPasswordToken, other.resetPasswordToken) &&
                Objects.equals(dailyMealPlans, other.dailyMealPlans);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, role, calories, height, weight, goal,
            accessToken, verificationToken, resetPasswordToken, dailyMealPlans);
    }
}
