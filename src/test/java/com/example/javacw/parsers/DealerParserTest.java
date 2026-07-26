package com.example.javacw.parsers;

import com.example.javacw.objects.Dealer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DealerParserTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Test parsing valid dealer file with different delimiters")
    void testParseDealerFileValid() throws IOException {
        Path file = tempDir.resolve("dealers.txt");
        String content = "D001, AutoZone, 123456, NY\n" +
                "D002| Speed Parts| 987654| Chicago\n" +
                "D003; Quick Fix; 555123; L.A.\n";
        Files.writeString(file, content);

        ArrayList<Dealer> dealers = DealerParser.parseDealerFile(file.toString());

        assertEquals(3, dealers.size());
        assertEquals("D001", dealers.get(0).getDealerId());
        assertEquals("AutoZone", dealers.get(0).getName());
        assertEquals("D002", dealers.get(1).getDealerId());
        assertEquals("Speed Parts", dealers.get(1).getName());
        assertEquals("D003", dealers.get(2).getDealerId());
    }

    @Test
    @DisplayName("Test skipping invalid dealer codes and empty lines")
    void testParseDealerFileInvalidAndEmptyLines() throws IOException {
        Path file = tempDir.resolve("dealers_invalid.txt");
        String content = "\n" +
                "INVALID_CODE, Bad Dealer, 000, Nowhere\n" +
                "D005, Good Dealer, 111, Boston\n" +
                "\n";
        Files.writeString(file, content);

        ArrayList<Dealer> dealers = DealerParser.parseDealerFile(file.toString());

        assertEquals(1, dealers.size());
        assertEquals("D005", dealers.get(0).getDealerId());
    }
}