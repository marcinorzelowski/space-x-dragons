package com.orzelowski.spacex.dragons.util;


public class ValidationUtils {
    private ValidationUtils() {}

    public static void validateString(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        if (text.trim().isEmpty()) {
            throw new IllegalArgumentException("Value cannot be empty");
        }
    }
}
