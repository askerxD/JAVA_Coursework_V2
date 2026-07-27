package com.example.javacw.utils;

import com.example.javacw.objects.Part;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SearchUtilTest {

    private ArrayList<Part> sampleParts;

    @BeforeEach
    void setUp() {
        sampleParts = new ArrayList<>();
        sampleParts.add(new Part("P001", "Brake Pad", "Brembo", 50.0, 10, "BRAKES", "01/01/2026", "img1.png"));
        sampleParts.add(new Part("P002", "Oil Filter", "Bosch", 15.0, 20, "ENGINE", "01/01/2026", "img2.png"));
        sampleParts.add(new Part("P003", "Spark Plug", "NGK", 10.0, 30, "ENGINE", "01/01/2026", "img3.png"));
    }

    @Test
    @DisplayName("Test filter by keyword across code, name, and brand")
    void testFilterByKeyword() {
        ArrayList<Part> result = SearchUtil.filterParts(sampleParts, "Brembo", "", "", "");
        assertEquals(1, result.size());
        assertEquals("P001", result.get(0).getPartCode());

        ArrayList<Part> filterResult = SearchUtil.filterParts(sampleParts, "Filter", "", "", "");
        assertEquals(1, filterResult.size());
        assertEquals("P002", filterResult.get(0).getPartCode());
    }

    @Test
    @DisplayName("Test filter by Category")
    void testFilterByCategory() {
        ArrayList<Part> result = SearchUtil.filterParts(sampleParts, "", "ENGINE", "", "");
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Test filter by Price Range")
    void testFilterByPriceRange() {
        // Price between 12.0 and 60.0 should return P001 ($50) and P002 ($15)
        ArrayList<Part> result = SearchUtil.filterParts(sampleParts, "", "", "12.0", "60.0");
        assertEquals(2, result.size());
    }
}

