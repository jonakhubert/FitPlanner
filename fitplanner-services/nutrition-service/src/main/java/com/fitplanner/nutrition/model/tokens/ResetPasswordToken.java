package com.fitplanner.nutrition.model.tokens;

import java.time.LocalDateTime;
import java.util.Objects;

public class ResetPasswordToken {

    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;

    public ResetPasswordToken() {}

    public ResetPasswordToken(String token, LocalDateTime createdAt, LocalDateTime expiredAt) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof ResetPasswordToken other))
            return false;

        return  Objects.equals(token, other.token) &&
                Objects.equals(createdAt, other.createdAt) &&
                Objects.equals(expiredAt, other.expiredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, createdAt, expiredAt);
    }
}
