package com.web_calling.backend.util;

public class StringUtils {

    public static String normalize(String input) {
        if (input == null) return null;

        return input
                .trim()
                .toLowerCase()
                .replace("_", " ")
                .replaceAll("\\s+", " ");
    }
}