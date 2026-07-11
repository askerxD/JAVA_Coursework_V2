package com.example.javacw.utils;

public class ValidationUtil {

    // Clean price values
    public static double parsePrice(String rawPrice) {
        if (rawPrice == null || rawPrice.trim().isEmpty()) {
            return 0.0;
        }
        String cleaned = rawPrice.trim();

        // remove currency symbols and text
        cleaned = cleaned.replace("Rs.", "")
                         .replace("Rs", "")
                         .replace(",", "")
                         .trim();
        try {
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    // Parse integer safely
    public static int parseQuantity(String rawQty) {
        if (rawQty == null || rawQty.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(rawQty.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    // Check if string is empty or missing
    public static String safeString(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }
    // Normalize category 
    public static String normalizeCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return "UNKNOWN";
        }
        return category.trim().toUpperCase();
    }
    // Check valid part code
    public static boolean isValidPartCode(String code) {
        return code != null && code.matches("P\\d+");
    }
    // Check valid dealer code
    public static boolean isValidDealerCode(String code) {
        return code != null && code.matches("D\\d+");
    }
}