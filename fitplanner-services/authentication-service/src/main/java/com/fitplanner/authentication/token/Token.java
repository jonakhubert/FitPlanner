package com.fitplanner.authentication.token;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "tokens")
public record Token(
    @Id String id,
    String token,
    @Enumerated(EnumType.STRING) TokenType tokenType,
    boolean expired,
    boolean revoked,
    String userEmail
) {}