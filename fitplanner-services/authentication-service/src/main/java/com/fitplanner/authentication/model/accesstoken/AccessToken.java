package com.fitplanner.authentication.model.access_token;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

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

    public Token() {}

    public void setTokenType(TokenType tokenType) { this.tokenType = tokenType; }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof Token other))
            return false;

        return  Objects.equals(id, other.id) &&
                Objects.equals(token, other.token) &&
                Objects.equals(tokenType, other.tokenType) &&
                Objects.equals(userEmail, other.userEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, tokenType, userEmail);
    }
}