package com.fitplanner.user.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ConfirmationResponse(
        @JsonProperty("confirmation_message") String message
) {}