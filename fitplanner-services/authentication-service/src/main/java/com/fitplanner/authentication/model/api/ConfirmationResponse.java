package com.fitplanner.authentication.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ConfirmationResponse(
        @JsonProperty("register_message") String message
) {}
