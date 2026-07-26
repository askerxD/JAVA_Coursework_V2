package com.example.javacw.objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DealerTest {

    @Test
    @DisplayName("Test parameterized constructor and getters")
    void testConstructorAndGetters() {
        Dealer dealer = new Dealer("D101", "AutoZone Supplies", "+1234567890", "New York");

        assertEquals("D101", dealer.getDealerId());
        assertEquals("AutoZone Supplies", dealer.getName());
        assertEquals("+1234567890", dealer.getContactNumber());
        assertEquals("New York", dealer.getLocation());
    }

    @Test
    @DisplayName("Test default constructor and setters")
    void testSetters() {
        Dealer dealer = new Dealer();
        dealer.setDealerId("D102");
        dealer.setName("Speedy Parts");
        dealer.setContactNumber("+0987654321");
        dealer.setLocation("Chicago");

        assertEquals("D102", dealer.getDealerId());
        assertEquals("Speedy Parts", dealer.getName());
        assertEquals("+0987654321", dealer.getContactNumber());
        assertEquals("Chicago", dealer.getLocation());
    }

    @Test
    @DisplayName("Test toString CSV-style format")
    void testToString() {
        Dealer dealer = new Dealer("D101", "AutoZone Supplies", "+1234567890", "New York");
        assertEquals("D101,AutoZone Supplies,+1234567890,New York", dealer.toString());
    }
}