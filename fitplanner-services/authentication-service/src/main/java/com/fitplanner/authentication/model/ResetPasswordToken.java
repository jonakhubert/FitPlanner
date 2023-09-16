package com.fitplanner.authentication.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

@Document(value = "reset_password_tokens")
public class ResetPasswordToken {

    @Id
    private String id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private String userEmail;

    public ResetPasswordToken() {}

    public ResetPasswordToken(String token, LocalDateTime createdAt, LocalDateTime expiredAt, String userEmail) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.userEmail = userEmail;
    }

    // getters
    public String getToken() { return token; }
    public LocalDateTime getExpiredAt() { return expiredAt; }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof ResetPasswordToken other))
            return false;

        return  Objects.equals(id, other.id) &&
                Objects.equals(token, other.token) &&
                Objects.equals(createdAt, other.createdAt) &&
                Objects.equals(expiredAt, other.expiredAt) &&
                Objects.equals(userEmail, other.userEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, createdAt, expiredAt, userEmail);
    }
}
