package com.fitplanner.authentication.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

@Document(value = "verification_tokens")
public class VerificationToken {

    @Id
    private String id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private LocalDateTime confirmedAt;
    private String userEmail;

    public VerificationToken() {}

    public VerificationToken(
        String token,
        LocalDateTime createdAt,
        LocalDateTime expiredAt,
        String userEmail
    ) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.userEmail = userEmail;
    }

    // getters
    public LocalDateTime getExpiredAt() { return expiredAt; }
    public LocalDateTime getConfirmedAt() { return confirmedAt; }
    public String getUserEmail() { return userEmail; }
    public String getToken() { return token; }

    // setters
    public void setConfirmedAt(LocalDateTime confirmedAt) { this.confirmedAt = confirmedAt; }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof VerificationToken other))
            return false;

        return  Objects.equals(id, other.id) &&
                Objects.equals(token, other.token) &&
                Objects.equals(createdAt, other.createdAt) &&
                Objects.equals(expiredAt, other.expiredAt) &&
                Objects.equals(confirmedAt, other.confirmedAt) &&
                Objects.equals(userEmail, other.userEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, createdAt, expiredAt, confirmedAt, userEmail);
    }
}
