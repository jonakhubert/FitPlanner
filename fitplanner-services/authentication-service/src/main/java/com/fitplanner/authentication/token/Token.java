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
    private TokenType tokenType = TokenType.BEARER;

    private String userEmail;

    public Token(String token, String userEmail) {
        this.token = token;
        this.userEmail = userEmail;
    }

    public void setTokenType(TokenType tokenType) { this.tokenType = tokenType; }
}