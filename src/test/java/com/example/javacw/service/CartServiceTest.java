package com.example.javacw.service;

import com.example.javacw.objects.Part;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {

    private CartService cartService;
    private Part enginePart;
    private Part electricalPart;

    @BeforeEach
    void setUp() {
        cartService = new CartService();
        enginePart = new Part("P001", "Engine Block", "BrandA", 100.0, 10, "ENGINE", "2026-01-01", "img.png");
        electricalPart = new Part("P002", "Alternator", "BrandB", 200.0, 10, "ELECTRICAL", "2026-01-01", "img.png");
    }

    @Test
    @DisplayName("Test adding items to cart and checking quantities")
    void testAddToCart() {
        assertTrue(cartService.addToCart(enginePart, 2));
        assertEquals(2, cartService.getQuantityInCart("P001"));

        assertTrue(cartService.addToCart(enginePart, 3));
        assertEquals(5, cartService.getQuantityInCart("P001"));

        assertFalse(cartService.addToCart(enginePart, 10));
    }

    @Test
    @DisplayName("Test removing items and quantities from cart")
    void testRemoveFromCart() {
        cartService.addToCart(enginePart, 5);
        assertTrue(cartService.removeQuantityFromCart("P001", 2));
        assertEquals(3, cartService.getQuantityInCart("P001"));

        cartService.removeFromCart("P001");
        assertEquals(0, cartService.getQuantityInCart("P001"));
    }

    @Test
    @DisplayName("Test bulk discount calculation (>= 3 items gets 5% off)")
    void testBulkDiscount() {
        cartService.addToCart(enginePart, 3);

        // 300 * 0.95 = 285.0
        assertEquals(300.0, cartService.getSubtotalBeforeDiscounts());
        assertEquals(285.0, cartService.getTotalAfterBulkDiscount());
        assertEquals(15.0, cartService.getBulkDiscountAmount());
    }

    @Test
    @DisplayName("Test synergy discount calculation (ENGINE + ELECTRICAL gets 10% off final)")
    void testSynergyDiscount() {
        cartService.addToCart(enginePart, 1);
        cartService.addToCart(electricalPart, 1);

        assertEquals(270.0, cartService.calculateTotal());
    }
}