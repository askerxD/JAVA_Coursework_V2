package com.example.javacw.service;

import com.example.javacw.objects.Part;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PartServiceTest {

    @TempDir
    Path tempDir;

    private InventoryService inventoryService;
    private PartService partService;

    @BeforeEach
    void setUp() throws IOException {
        Path tempFile = tempDir.resolve("inventory.txt");
        String initialData = "P001, Brake Pad, Brembo, 50.0, 10, BRAKES, 01/01/2026, brake.png, 5\n";
        Files.writeString(tempFile, initialData);

        inventoryService = new InventoryService(tempFile.toString());
        partService = new PartService(inventoryService);
    }

    @Test
    @DisplayName("Test partCodeExists returns true for existing code and false for new code")
    void testPartCodeExists() {
        assertTrue(partService.partCodeExists("P001"));
        assertFalse(partService.partCodeExists("P999"));
    }

    @Test
    @DisplayName("Test adding valid part successfully")
    void testAddPartSuccess() {
        Part newPart = new Part("P002", "Oil Filter", "Bosch", 15.0, 5, "ENGINE", "01/01/2026", "filter.png", 5);
        assertDoesNotThrow(() -> partService.addPart(newPart));
        assertTrue(partService.partCodeExists("P002"));
    }

    @Test
    @DisplayName("Test adding duplicate part code throws IllegalArgumentException")
    void testAddPartDuplicateThrowsException() {
        Part duplicatePart = new Part("P001", "Another Brake Pad", "Brembo", 50.0, 10, "BRAKES", "01/01/2026", "brake.png", 5);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> partService.addPart(duplicatePart)
        );
        assertEquals("Part code 'P001' already exists.", exception.getMessage());
    }

    @Test
    @DisplayName("Test validatePartData passes for valid inputs")
    void testValidatePartDataSuccess() {
        assertDoesNotThrow(() -> partService.validatePartData(
                "P002", "Spark Plug", "NGK", 25.0, 10, "IGNITION", LocalDate.now(), "plug.png", 5
        ));
    }

    @Test
    @DisplayName("Test validatePartData throws exception for empty/invalid fields")
    void testValidatePartDataFailures() {
        assertThrows(IllegalArgumentException.class, () -> partService.validatePartData(
                "", "Spark Plug", "NGK", 25.0, 10, "IGNITION", LocalDate.now(), "plug.png", 5
        ));

        assertThrows(IllegalArgumentException.class, () -> partService.validatePartData(
                "INVALID123", "Spark Plug", "NGK", 25.0, 10, "IGNITION", LocalDate.now(), "plug.png", 5
        ));

        assertThrows(IllegalArgumentException.class, () -> partService.validatePartData(
                "P002", "", "NGK", 25.0, 10, "IGNITION", LocalDate.now(), "plug.png", 5
        ));

        assertThrows(IllegalArgumentException.class, () -> partService.validatePartData(
                "P002", "Spark Plug", "NGK", -10.0, 10, "IGNITION", LocalDate.now(), "plug.png", 5
        ));

        assertThrows(IllegalArgumentException.class, () -> partService.validatePartData(
                "P002", "Spark Plug", "NGK", 25.0, -5, "IGNITION", LocalDate.now(), "plug.png", 5
        ));

        assertThrows(IllegalArgumentException.class, () -> partService.validatePartData(
                "P002", "Spark Plug", "NGK", 25.0, 10, "IGNITION", null, "plug.png", 5
        ));
    }
}