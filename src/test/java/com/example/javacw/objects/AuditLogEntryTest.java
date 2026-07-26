package com.example.javacw.objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuditLogEntryTest {

    @Test
    @DisplayName("Test Constructor and Getters for JavaFX String Properties")
    void testConstructorAndGetters() {
        AuditLogEntry entry = new AuditLogEntry(
                "2026-03-30 10:15:00",
                "ADMIN",
                "DELETE",
                "PART",
                "Deleted Part P001"
        );

        assertEquals("2026-03-30 10:15:00", entry.getTimestamp());
        assertEquals("ADMIN", entry.getRole());
        assertEquals("DELETE", entry.getAction());
        assertEquals("PART", entry.getEntity());
        assertEquals("Deleted Part P001", entry.getDetails());
    }
}