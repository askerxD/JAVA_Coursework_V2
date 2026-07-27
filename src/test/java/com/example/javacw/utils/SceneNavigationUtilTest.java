package com.example.javacw.utils;

import javafx.application.Platform;
import javafx.scene.control.Button;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class SceneNavigationUtilTest {

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException e) {
            latch.countDown();
        }
        assertTrue(latch.await(5, TimeUnit.SECONDS), "JavaFX Toolkit failed to initialize");
    }

    @Test
    @DisplayName("Test setupNavigationButtons binds event handlers to non-null buttons")
    void testSetupNavigationButtonsWithValidButtons() {
        Button inventoryBtn = new Button("Dashboard");
        Button posBtn = new Button("POS");
        Button dealerBtn = new Button("Dealers");
        Button auditBtn = new Button("Audit Log");

        SceneNavigationUtil.setupNavigationButtons(inventoryBtn, posBtn, dealerBtn, auditBtn);

        assertNotNull(inventoryBtn.getOnAction(), "Inventory button should have an action handler set");
        assertNotNull(posBtn.getOnAction(), "POS button should have an action handler set");
        assertNotNull(dealerBtn.getOnAction(), "Dealer button should have an action handler set");
        assertNotNull(auditBtn.getOnAction(), "Audit button should have an action handler set");
    }

    @Test
    @DisplayName("Test setupNavigationButtons safely handles null button references")
    void testSetupNavigationButtonsWithNulls() {
        Button inventoryBtn = new Button("Dashboard");

        assertDoesNotThrow(() ->
                SceneNavigationUtil.setupNavigationButtons(inventoryBtn, null, null, null)
        );

        assertNotNull(inventoryBtn.getOnAction());
    }
}