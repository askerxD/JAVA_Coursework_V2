package com.example.javacw.objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PartTest {

    private Part part;

    @BeforeEach
    void setUp() {
        part = new Part("P001", "Brake Pad", "Brembo", 45.50, 10, "Brakes", "2026-01-01", "brake.png");
    }

    @Test
    @DisplayName("Test default constructor and default threshold set to 5")
    void testConstructorWithoutThreshold() {
        assertEquals("P001", part.getPartCode());
        assertEquals("Brake Pad", part.getName());
        assertEquals("Brembo", part.getBrand());
        assertEquals(45.50, part.getPrice(), 0.001);
        assertEquals(10, part.getQuantity());
        assertEquals("Brakes", part.getCategory());
        assertEquals("2026-01-01", part.getDateAdded());
        assertEquals("brake.png", part.getImage());
        assertEquals(5, part.getLowStockThreshold()); // Default threshold
    }

    @Test
    @DisplayName("Test constructor with custom low stock threshold")
    void testConstructorWithThreshold() {
        Part customPart = new Part("P002", "Oil Filter", "Bosch", 12.00, 3, "Engine", "2026-01-02", "filter.png", 8);
        assertEquals(8, customPart.getLowStockThreshold());
    }

    @Test
    @DisplayName("Test no-args constructor and setters")
    void testSettersAndGetters() {
        Part emptyPart = new Part();
        emptyPart.setPartCode("P003");
        emptyPart.setName("Spark Plug");
        emptyPart.setBrand("NGK");
        emptyPart.setPrice(15.00);
        emptyPart.setQuantity(20);
        emptyPart.setCategory("Ignition");
        emptyPart.setDateAdded("2026-01-03");
        emptyPart.setImage("plug.png");
        emptyPart.setLowStockThreshold(10);

        assertEquals("P003", emptyPart.getPartCode());
        assertEquals("Spark Plug", emptyPart.getName());
        assertEquals("NGK", emptyPart.getBrand());
        assertEquals(15.00, emptyPart.getPrice(), 0.001);
        assertEquals(20, emptyPart.getQuantity());
        assertEquals("Ignition", emptyPart.getCategory());
        assertEquals("2026-01-03", emptyPart.getDateAdded());
        assertEquals("plug.png", emptyPart.getImage());
        assertEquals(10, emptyPart.getLowStockThreshold());
    }

    @Test
    @DisplayName("Test total value calculation")
    void testGetTotalValue() {
        // 45.50 * 10 = 455.0
        assertEquals(455.00, part.getTotalValue(), 0.001);
    }

    @Test
    @DisplayName("Test toString formatted output")
    void testToString() {
        String expected = "P001,Brake Pad,Brembo,45.5,10,Brakes,2026-01-01,brake.png,5";
        assertEquals(expected, part.toString());
    }
}