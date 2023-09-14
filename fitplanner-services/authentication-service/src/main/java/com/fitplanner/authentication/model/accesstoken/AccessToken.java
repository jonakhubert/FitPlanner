package com.fitplanner.authentication.model.accesstoken;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(value = "access_tokens")
public class AccessToken {

    @Id
    private String id;
    private String token;
    @Enumerated(EnumType.STRING)
    private AccessTokenType tokenType = AccessTokenType.BEARER;
    private String userEmail;

    public AccessToken(String token, String userEmail) {
        this.token = token;
        this.userEmail = userEmail;
    }

    public AccessToken() {}

    public void setTokenType(AccessTokenType tokenType) { this.tokenType = tokenType; }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof AccessToken other))
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