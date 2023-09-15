package com.fitplanner.authentication.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterResponse(
        @JsonProperty("verification_message") String message
) {}
