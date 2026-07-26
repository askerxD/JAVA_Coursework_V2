package com.example.javacw.parsers;

import com.example.javacw.objects.Part;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InventoryParserTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Test parsing standard inventory lines")
    void testParseInventoryFile() throws IOException {
        Path file = tempDir.resolve("inventory.txt");
        String content = "P001, Brake Pad, Brembo, 45.5, 10, Brakes, 2026-01-01, brake.png\n" +
                "P002| Oil Filter| Bosch| 12.0| 5| Engine| 2026-01-02| filter.png\n";
        Files.writeString(file, content);

        ArrayList<Part> parts = InventoryParser.parseInventoryFile(file.toString());

        assertEquals(2, parts.size());
        assertEquals("P001", parts.get(0).getPartCode());
        assertEquals(45.5, parts.get(0).getPrice());
        assertEquals("P002", parts.get(1).getPartCode());
    }

    @Test
    @DisplayName("Test date comma legacy splitting protection")
    void testParseLegacyDateWithComma() throws IOException {
        Path file = tempDir.resolve("legacy_inventory.txt");
        String content = "P003, Spark Plug, NGK, 15.0, 20, Engine, Jan 12, 2026, plug.png\n";
        Files.writeString(file, content);

        ArrayList<Part> parts = InventoryParser.parseInventoryFile(file.toString());

        assertEquals(1, parts.size());
        assertEquals("P003", parts.get(0).getPartCode());
    }
}