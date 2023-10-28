package com.fitplanner.authentication.model.tokens;

import java.time.LocalDateTime;
import java.util.Objects;

public class VerificationToken {

    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private LocalDateTime confirmedAt;

    public VerificationToken() {}

    public VerificationToken(String token, LocalDateTime createdAt, LocalDateTime expiredAt) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
    }

    // getters
    public LocalDateTime getExpiredAt() { return expiredAt; }
    public LocalDateTime getConfirmedAt() { return confirmedAt; }
    public String getToken() { return token; }

    // setters
    public void setConfirmedAt(LocalDateTime confirmedAt) { this.confirmedAt = confirmedAt; }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof VerificationToken other))
            return false;

        return  Objects.equals(token, other.token) &&
                Objects.equals(createdAt, other.createdAt) &&
                Objects.equals(expiredAt, other.expiredAt) &&
                Objects.equals(confirmedAt, other.confirmedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, createdAt, expiredAt, confirmedAt);
    }
}
