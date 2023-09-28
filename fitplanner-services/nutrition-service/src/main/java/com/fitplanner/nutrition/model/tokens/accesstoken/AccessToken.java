package com.fitplanner.nutrition.model.tokens.accesstoken;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class AccessToken {

    private String token;
    @Enumerated(EnumType.STRING)
    private AccessTokenType tokenType = AccessTokenType.BEARER;

    public AccessToken(String token) {
        this.token = token;
    }

    public AccessToken() {}
}