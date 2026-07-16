package com.example.javacw.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

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

    public static boolean isNonNegativePrice(double price) {
        return price >= 0;
    }

    public static boolean isNonNegativeQuantity(int quantity) {
        return quantity >= 0;
    }

    // Standardize date to dd/mm/yyyy format
    public static String standardizeDate(String rawDate) {
        if (rawDate == null || rawDate.trim().isEmpty()) {
            return "";
        }

        String trimmed = rawDate.trim();
        DateTimeFormatter targetFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Try various date formats
        String[] formats = {
                "yyyy-MM-dd",      // 2023-10-12
                "dd/MM/yyyy",      // 12/05/2023
                "MMM dd, yyyy",    // Oct 15, 2023
                "dd-MMM-yyyy",     // 15-Aug-2023
                "yyyy/MM/dd",      // 2023/11/20
                "dd-MM-yyyy"       // 01-02-2024
        };

        for (String format : formats) {
            try {
                LocalDate date = LocalDate.parse(trimmed, DateTimeFormatter.ofPattern(format, Locale.ENGLISH));
                return date.format(targetFormatter);
            } catch (DateTimeParseException e) {
                // Try next format
            }
        }

        return trimmed;
    }
}
