package com.fitplanner.authentication.token;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "tokens")
public class Token {

    @Id
    private String id;

    private String token;

    @Enumerated(EnumType.STRING)
    private final TokenType tokenType = TokenType.BEARER;

    private boolean expired;

    private boolean revoked;

    private String userEmail;

    public Token(String token, boolean expired, boolean revoked, String userEmail) {
        this.token = token;
        this.expired = expired;
        this.revoked = revoked;
        this.userEmail = userEmail;
    }

    public void setExpired(boolean expired) { this.expired = expired; }
    public void setRevoked(boolean revoked) { this.revoked = revoked; }
    //public void setTokenType(TokenType tokenType) { this.tokenType = tokenType; }
    public boolean isExpired() { return expired; }
    public boolean isRevoked() { return revoked; }
}