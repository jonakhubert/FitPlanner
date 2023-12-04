package com.fitplanner.workout.model.api;

public record ApiError(
        String path,
        String message,
        int statusCode,
        String timestamp
) {}
