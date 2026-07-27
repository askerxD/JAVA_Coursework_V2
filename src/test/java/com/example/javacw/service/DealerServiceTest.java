package com.example.javacw.service;

import com.example.javacw.objects.Dealer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DealerServiceTest {

    @TempDir
    Path tempDir;

    private Path dealerFile;

    @BeforeEach
    void setUp() throws IOException {
        dealerFile = tempDir.resolve("dealers.txt");
        String content = "D001, AutoZone, 123456, New York\n" +
                         "D002, Speed Parts, 987654, Chicago\n" +
                         "D003, Quick Fix, 555123, Boston\n" +
                         "D004, Apex Auto, 444555, Atlanta\n" +
                         "D005, Metro Spares, 777888, Denver\n";
        Files.writeString(dealerFile, content);
    }

    @Test
    @DisplayName("Test loading all dealers from file")
    void testGetAllDealers() {
        DealerService dealerService = new DealerService(dealerFile.toString());
        ArrayList<Dealer> dealers = dealerService.getAllDealers();

        assertEquals(5, dealers.size());
    }

    @Test
    @DisplayName("Test getRandomFourDealers returns exactly 4 unique sorted dealers when pool > 4")
    void testGetRandomFourDealers() {
        DealerService dealerService = new DealerService(dealerFile.toString());
        ArrayList<Dealer> randomFour = dealerService.getRandomFourDealers();

        assertEquals(4, randomFour.size());

        // Check uniqueness of dealer IDs
        long uniqueCount = randomFour.stream()
                .map(Dealer::getDealerId)
                .distinct()
                .count();
        assertEquals(4, uniqueCount);

        // Verify location sorting (e.g., Atlanta <= Boston <= Chicago...)
        for (int i = 0; i < randomFour.size() - 1; i++) {
            String currentLoc = randomFour.get(i).getLocation().toLowerCase();
            String nextLoc = randomFour.get(i + 1).getLocation().toLowerCase();
            assertTrue(currentLoc.compareTo(nextLoc) <= 0);
        }
    }

    @Test
    @DisplayName("Test getRandomFourDealers returns all dealers sorted when pool <= 4")
    void testGetRandomFourDealersSmallPool() throws IOException {
        Path smallFile = tempDir.resolve("small_dealers.txt");
        String content = "D001, AutoZone, 123, New York\n" +
                         "D002, Speed Parts, 456, Chicago\n";
        Files.writeString(smallFile, content);

        DealerService smallService = new DealerService(smallFile.toString());
        ArrayList<Dealer> result = smallService.getRandomFourDealers();

        assertEquals(2, result.size());
        assertEquals("Chicago", result.get(0).getLocation());
        assertEquals("New York", result.get(1).getLocation());
    }
}