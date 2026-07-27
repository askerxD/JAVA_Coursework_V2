package com.example.javacw.utils;

import com.example.javacw.objects.Dealer;
import com.example.javacw.objects.Part;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SortUtilTest {

    @Test
    @DisplayName("Test sortDealersByLocation (Bubble Sort alphabetically by location)")
    void testSortDealersByLocation() {
        ArrayList<Dealer> dealers = new ArrayList<>();
        dealers.add(new Dealer("D001", "Dealer A", "123", "New York"));
        dealers.add(new Dealer("D002", "Dealer B", "456", "Chicago"));
        dealers.add(new Dealer("D003", "Dealer C", "789", "Boston"));

        SortUtil.sortDealersByLocation(dealers);

        assertEquals("Boston", dealers.get(0).getLocation());
        assertEquals("Chicago", dealers.get(1).getLocation());
        assertEquals("New York", dealers.get(2).getLocation());
    }

    @Test
    @DisplayName("Test sortPartCatCode sorts primary by Category and secondary by Part Code")
    void testSortPartCatCode() {
        ArrayList<Part> parts = new ArrayList<>();
        parts.add(new Part("P003", "Brake Disc", "BrandA", 100.0, 5, "BRAKES", "01/01/2026", "img.png"));
        parts.add(new Part("P001", "Brake Pad", "BrandA", 50.0, 5, "BRAKES", "01/01/2026", "img.png"));
        parts.add(new Part("P002", "Oil Filter", "BrandB", 20.0, 5, "ENGINE", "01/01/2026", "img.png"));

        SortUtil.sortPartCatCode(parts);

        assertEquals("P001", parts.get(0).getPartCode());
        assertEquals("P003", parts.get(1).getPartCode());
        assertEquals("P002", parts.get(2).getPartCode());
    }
}