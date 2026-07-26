package com.example.javacw.service;

import com.example.javacw.objects.AuditLogEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AuditServiceTest {

    @TempDir
    Path tempDir;

    private AuditService auditService;
    private String logFilePath;

    @BeforeEach
    void setUp() {
        logFilePath = tempDir.resolve("audit_log.txt").toString();
        auditService = new AuditService(logFilePath);
    }

    @Test
    @DisplayName("Test logging inventory addition and reading back entries")
    void testWriteAndReadLog() {
        auditService.logInventoryAddAsAdmin("P001", 10);
        auditService.logSaleAsCashier("partCode=P001, quantitySold=2");

        ArrayList<AuditLogEntry> entries = auditService.readAll();

        assertEquals(2, entries.size());
        assertEquals("ADMIN", entries.get(0).getRole());
        assertEquals("ADD", entries.get(0).getAction());
        assertEquals("P001", entries.get(0).getEntity());

        assertEquals("CASHIER", entries.get(1).getRole());
        assertEquals("SALE", entries.get(1).getAction());
    }

    @Test
    @DisplayName("Test exporting log to a new path")
    void testExportAuditLog() throws IOException {
        auditService.logInventoryDeleteAsAdmin("P002");
        String exportPath = tempDir.resolve("exported_logs/export.txt").toString();

        auditService.exportAuditLog(exportPath);

        AuditService exportedService = new AuditService(exportPath);
        ArrayList<AuditLogEntry> exportedEntries = exportedService.readAll();

        assertEquals(1, exportedEntries.size());
        assertEquals("DELETE", exportedEntries.get(0).getAction());
    }
}