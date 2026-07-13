package com.example.javacw;

import com.example.javacw.utils.SceneNavigationUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class POSCheckoutController implements Initializable {
    @FXML
    private Button inventoryDashboardButton;
    @FXML
    private Button posCheckoutButton;
    @FXML
    private Button dealerSelectionButton;
    @FXML
    private Button auditLoButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SceneNavigationUtil.setupNavigationButtons(
                inventoryDashboardButton,
                posCheckoutButton,
                dealerSelectionButton,
                auditLoButton
        );
    }
}
