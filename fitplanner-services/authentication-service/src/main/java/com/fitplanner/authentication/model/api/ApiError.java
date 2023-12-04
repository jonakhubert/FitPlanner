package com.fitplanner.authentication.model.api;

public record ApiError(
        String path,
        String message,
        int statusCode,
        String timestamp
) {}
