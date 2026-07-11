package com.example.javacw;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class HelloController {
     @FXML
     private Button inventoryDashboardButton;
     @FXML
     private Button posCheckoutButton;
     @FXML
     private Button dealerSelectionButton;
     @FXML
     private Button auditLoButton;
     @FXML
     private Button addNewPartButton;
     @FXML
     private Button updateSelectedButton;
     @FXML
     private Button deleteSelectedButton;
     @FXML
     private Button lowStockThresholdSave;
     @FXML
     private TextField keyworkSearchField;
     @FXML
     private TextField minPriceField;
     @FXML
     private TextField maxPriceField;
     @FXML
     private TextField lowStockThreshold;
     @FXML
     private TableView inventoryTable;
     @FXML
     private Label totalnventoryValue;
     @FXML
     private Label partsCount;
     @FXML
     private VBox lowStockWarning;



}
