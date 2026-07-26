package com.example.javacw.service;

import com.example.javacw.objects.Part;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartServiceTest {

    @Mock
    private InventoryService inventoryService;

    private PartService partService;

    @BeforeEach
    void setUp() {
        partService = new PartService(inventoryService);
    }

    @Test
    @DisplayName("Test validation passes for valid inputs")
    void testValidatePartDataSuccess() {
        assertDoesNotThrow(() -> partService.validatePartData(
                "P001", "Brake Pad", "Brembo", 50.0, 10, "Brakes", LocalDate.now(), "img.png", 5
        ));
    }

    @Test
    @DisplayName("Test validation throws exception for invalid part code or negative values")
    void testValidatePartDataFailures() {
        // Invalid Part Code
        assertThrows(IllegalArgumentException.class, () -> partService.validatePartData(
                "INVALID", "Brake Pad", "Brembo", 50.0, 10, "Brakes", LocalDate.now(), "img.png", 5
        ));

        // Negative Price
        assertThrows(IllegalArgumentException.class, () -> partService.validatePartData(
                "P001", "Brake Pad", "Brembo", -10.0, 10, "Brakes", LocalDate.now(), "img.png", 5
        ));
    }

    @Test
    @DisplayName("Test adding duplicate part throws exception")
    void testAddPartDuplicate() {
        Part part = new Part("P001", "Filter", "Bosch", 10.0, 5, "Engine", "2026-01-01", "img.png");
        when(inventoryService.partCodeExists("P001")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> partService.addPart(part));
    }
}