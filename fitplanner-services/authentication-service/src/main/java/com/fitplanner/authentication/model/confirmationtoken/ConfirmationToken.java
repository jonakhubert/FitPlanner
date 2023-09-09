package com.fitplanner.authentication.model.confirmationtoken;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(value = "confirmation_tokens")
public class ConfirmationToken {

    @Id
    private String id;

    private String token;

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    private LocalDateTime confirmedAt;

    private String userEmail;

    public ConfirmationToken() {}

    public ConfirmationToken(
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

    // setters
    public void setConfirmedAt(LocalDateTime confirmedAt) { this.confirmedAt = confirmedAt; }
}
