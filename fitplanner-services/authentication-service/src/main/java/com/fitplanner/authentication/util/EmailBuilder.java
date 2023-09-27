package com.fitplanner.authentication.util;

public class EmailBuilder {
    public static String buildEmailBody(String link, String message) {
        return "<a href=\"" + link + "\">" + message + "</a>";
    }
}
