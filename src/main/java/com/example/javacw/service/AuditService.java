package com.example.javacw.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private final String filePath;
    public AuditService(String filePath) {
        this.filePath = filePath;
    }
    // Log ADD action
    public void logAdd(String partCode, int quantity) {
        writeLog("ADD", partCode, quantity);
    }
    // Log DELETE action
    public void logDelete(String partCode) {
        writeLog("DELETE", partCode, 0);
    }
    // Log CHECKOUT action
    public void logCheckout(String partCode, int quantity) {
        writeLog("CHECKOUT", partCode, quantity);
    }
    // Core logging method
    private void writeLog(String action, String partCode, int quantity) {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logEntry = timestamp +
                " | " + action +
                " | " + partCode +
                " | " + quantity;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(logEntry);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error writing audit log: " + e.getMessage());
        }
    }
}