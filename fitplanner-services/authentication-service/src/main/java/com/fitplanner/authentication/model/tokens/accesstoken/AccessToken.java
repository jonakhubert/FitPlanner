package com.fitplanner.authentication.model.tokens.accesstoken;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.Objects;

public class AccessToken {

    private String token;
    @Enumerated(EnumType.STRING)
    private AccessTokenType tokenType = AccessTokenType.BEARER;

    public AccessToken(String token) {
        this.token = token;
    }

    public AccessToken() {}

    // getters
    public String getToken() { return token; }

    // setters
    public void setTokenType(AccessTokenType tokenType) { this.tokenType = tokenType; }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof AccessToken other))
            return false;

        return  Objects.equals(token, other.token) &&
                Objects.equals(tokenType, other.tokenType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, tokenType);
    }
}