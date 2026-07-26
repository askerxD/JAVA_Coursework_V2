package com.example.javacw.objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {

    private Part testPart;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        testPart = new Part("P001", "Brake Pad", "Brembo", 50.00, 100, "Brakes", "2026-01-01", "brake.png");
        cartItem = new CartItem(testPart, 3);
    }

    @Test
    @DisplayName("Test parameterized constructor and getters")
    void testConstructorAndGetters() {
        assertEquals(testPart, cartItem.getPart());
        assertEquals(3, cartItem.getQuantity());
    }

    @Test
    @DisplayName("Test no-args constructor and setters")
    void testSetters() {
        CartItem item = new CartItem();
        item.setPart(testPart);
        item.setQuantity(5);

        assertEquals(testPart, item.getPart());
        assertEquals(5, item.getQuantity());
    }

    @Test
    @DisplayName("Test subtotal calculation")
    void testGetSubtotal() {
        // Price: 50.00, Quantity: 3 -> Subtotal: 150.00
        assertEquals(150.00, cartItem.getSubtotal(), 0.001);
    }

    @Test
    @DisplayName("Test toString output format")
    void testToString() {
        assertEquals("P001 x 3", cartItem.toString());
    }
}