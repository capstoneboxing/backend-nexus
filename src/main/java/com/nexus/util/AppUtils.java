package com.nexus.util;

public final class AppUtils {

    private AppUtils() {

    }

    public static String extractJson(String response) {
        int start = response.indexOf("{");
        int end = response.lastIndexOf("}");

        if (start == -1 || end == -1 || end < start) {
            throw new RuntimeException("No valid JSON object found in AI response: " + response);
        }

        return response.substring(start, end + 1);
    }

    public static double roundTo2DecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}