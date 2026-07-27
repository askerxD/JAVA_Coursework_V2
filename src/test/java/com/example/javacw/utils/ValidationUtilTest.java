package com.example.javacw.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    @DisplayName("Test parsePrice with currency prefixes, commas, and invalid strings")
    void testParsePrice() {
        assertEquals(1500.50, ValidationUtil.parsePrice("Rs. 1,500.50"), 0.001);
        assertEquals(250.00, ValidationUtil.parsePrice("Rs 250"), 0.001);
        assertEquals(45.00, ValidationUtil.parsePrice("45.00"), 0.001);
        assertEquals(0.0, ValidationUtil.parsePrice("invalid_price"), 0.001);
        assertEquals(0.0, ValidationUtil.parsePrice(null), 0.001);
    }

    @Test
    @DisplayName("Test parseQuantity with valid and invalid values")
    void testParseQuantity() {
        assertEquals(10, ValidationUtil.parseQuantity("10"));
        assertEquals(0, ValidationUtil.parseQuantity("invalid"));
        assertEquals(0, ValidationUtil.parseQuantity(null));
        assertEquals(0, ValidationUtil.parseQuantity(""));
    }

    @Test
    @DisplayName("Test safeString handles null and whitespace")
    void testSafeString() {
        assertEquals("hello", ValidationUtil.safeString("  hello  "));
        assertEquals("", ValidationUtil.safeString(null));
    }

    @Test
    @DisplayName("Test normalizeCategory upper-cases input or defaults to UNKNOWN")
    void testNormalizeCategory() {
        assertEquals("ENGINE", ValidationUtil.normalizeCategory("engine"));
        assertEquals("UNKNOWN", ValidationUtil.normalizeCategory(null));
        assertEquals("UNKNOWN", ValidationUtil.normalizeCategory("   "));
    }

    @Test
    @DisplayName("Test valid part codes matching 'P\\d+'")
    void testValidPartCodes() {
        assertTrue(ValidationUtil.isValidPartCode("P1"));
        assertTrue(ValidationUtil.isValidPartCode("P001"));
        assertTrue(ValidationUtil.isValidPartCode("P12345"));
    }

    @Test
    @DisplayName("Test invalid part codes")
    void testInvalidPartCodes() {
        assertFalse(ValidationUtil.isValidPartCode("123"));
        assertFalse(ValidationUtil.isValidPartCode("P"));
        assertFalse(ValidationUtil.isValidPartCode("p001"));
        assertFalse(ValidationUtil.isValidPartCode("PART1"));
        assertFalse(ValidationUtil.isValidPartCode(""));
        assertFalse(ValidationUtil.isValidPartCode(" "));
        assertFalse(ValidationUtil.isValidPartCode(null));
    }

    @Test
    @DisplayName("Test valid and invalid dealer codes matching 'D\\d+'")
    void testDealerCodes() {
        assertTrue(ValidationUtil.isValidDealerCode("D1"));
        assertTrue(ValidationUtil.isValidDealerCode("D999"));
        assertFalse(ValidationUtil.isValidDealerCode("123"));
        assertFalse(ValidationUtil.isValidDealerCode("dealer"));
    }

    @Test
    @DisplayName("Test standardizeDate converts various date formats to dd/MM/yyyy")
    void testStandardizeDate() {
        assertEquals("15/01/2026", ValidationUtil.standardizeDate("2026-01-15"));
        assertEquals("15/01/2026", ValidationUtil.standardizeDate("15/01/2026"));
        assertEquals("15/01/2026", ValidationUtil.standardizeDate("Jan 15, 2026"));
        assertEquals("15/01/2026", ValidationUtil.standardizeDate("15-Jan-2026"));
        assertEquals("15/01/2026", ValidationUtil.standardizeDate("2026/01/15"));
        assertEquals("15/01/2026", ValidationUtil.standardizeDate("15-01-2026"));
    }
}