package com.fitplanner.nutrition.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ConfirmationResponse(
        @JsonProperty("confirmation_message") String message
) {}
