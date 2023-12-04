package com.fitplanner.user.model.api;

public record ApiError(
        String path,
        String message,
        int statusCode,
        String timestamp
) {}
