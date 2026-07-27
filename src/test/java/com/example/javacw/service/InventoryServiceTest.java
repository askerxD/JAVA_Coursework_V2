package com.example.javacw.service;

import com.example.javacw.objects.Part;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {

    @TempDir
    Path tempDir;

    private Path inventoryFile;
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() throws IOException {
        inventoryFile = tempDir.resolve("inventory.txt");
        String content = "P001, Brake Pad, Brembo, 50.0, 10, BRAKES, 01/01/2026, brake.png, 5\n" +
                         "P002, Oil Filter, Bosch, 20.0, 3, ENGINE, 01/01/2026, filter.png, 5\n";
        Files.writeString(inventoryFile, content);
        inventoryService = new InventoryService(inventoryFile.toString());
    }

    @Test
    @DisplayName("Test finding parts by part code")
    void testGetPartByCode() {
        Part part = inventoryService.getPartByCode("p001");
        assertNotNull(part);
        assertEquals("Brake Pad", part.getName());

        assertNull(inventoryService.getPartByCode("P999"));
    }

    @Test
    @DisplayName("Test adding a new part and avoiding duplicate additions")
    void testAddPart() {
        Part newPart = new Part("P003", "Spark Plug", "NGK", 15.0, 20, "IGNITION", "01/01/2026", "plug.png", 5);
        assertTrue(inventoryService.addPart(newPart));
        assertTrue(inventoryService.partCodeExists("P003"));

        assertFalse(inventoryService.addPart(newPart));
    }

    @Test
    @DisplayName("Test updating an existing part")
    void testUpdatePart() {
        Part updatedPart = new Part("P001", "Updated Brake Pad", "Brembo", 55.0, 12, "BRAKES", "01/01/2026", "brake.png", 5);
        assertTrue(inventoryService.updatePart("P001", updatedPart));

        Part retrieved = inventoryService.getPartByCode("P001");
        assertEquals("Updated Brake Pad", retrieved.getName());
        assertEquals(55.0, retrieved.getPrice());
    }

    @Test
    @DisplayName("Test deleting a part")
    void testDeletePart() {
        assertTrue(inventoryService.deletePart("P001"));
        assertNull(inventoryService.getPartByCode("P001"));
        assertFalse(inventoryService.deletePart("P001")); // Already deleted
    }

    @Test
    @DisplayName("Test retrieving low stock items based on threshold")
    void testGetLowStockItems() {
        // Threshold set to 5: P002 has stock 3 (< 5), P001 has stock 10
        ArrayList<Part> lowStock = inventoryService.getLowStockItems(5);

        assertEquals(1, lowStock.size());
        assertEquals("P002", lowStock.get(0).getPartCode());
    }

    @Test
    @DisplayName("Test calculating total inventory value and total item count")
    void testInventoryCalculations() {
        // Total value = (50.0 * 10) + (20.0 * 3) = 500 + 60 = 560.0
        assertEquals(560.0, inventoryService.getTotalInventoryValue(), 0.001);

        // Total count = 10 + 3 = 13
        assertEquals(13, inventoryService.getTotalItemCount());
    }

    @Test
    @DisplayName("Test stock reduction logic")
    void testReduceStock() {
        assertTrue(inventoryService.reduceStock("P001", 4));
        assertEquals(6, inventoryService.getPartByCode("P001").getQuantity());

        // Attempting to sell more stock than available should fail
        assertFalse(inventoryService.reduceStock("P001", 100));

        // Invalid quantities (<= 0) should fail
        assertFalse(inventoryService.reduceStock("P001", 0));
        assertFalse(inventoryService.reduceStock("P001", -5));
    }
}