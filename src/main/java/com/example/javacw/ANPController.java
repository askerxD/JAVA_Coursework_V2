
package com.example.javacw;

import com.example.javacw.objects.Part;
import com.example.javacw.parsers.InventoryParser;
import com.example.javacw.utils.LowStockUtil;
import com.example.javacw.utils.SearchUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class ANPController implements Initializable {
    @FXML
    private TextField PartCode;
    @FXML
    private TextField Description;
    @FXML
    private TextField Brand;
    @FXML
    private TextField Price;
    @FXML
    private TextField StockQty;
    @FXML
    private ChoiceBox<String> Category;
    @FXML
    private DatePicker LastUpdated;
    @FXML
    private Button SavePart;
    @FXML
    private Button cancel;

    private HelloController parentController;

    public void setParentController(HelloController controller) {
        this.parentController = controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupCategoryDropdown();
        setupButtonHandlers();
    }

    private void setupCategoryDropdown() {
        Category.getItems().addAll("Engine", "Electrical", "Brakes", "Bodywork");
    }

    private void setupButtonHandlers() {
        cancel.setOnAction(event -> handleCancel());
        SavePart.setOnAction(event -> handleSavePart());
    }

    private void handleCancel() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Cancel");
        confirmAlert.setHeaderText("Cancel Operation?");
        confirmAlert.setContentText("Are you sure you want to cancel? Any unsaved data will be lost.");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            closeWindow();
        }
    }

    private void handleSavePart() {
        if (validateInput()) {
            try {
                String partCode = PartCode.getText().trim();
                String description = Description.getText().trim();
                String brand = Brand.getText().trim();
                double price = Double.parseDouble(Price.getText().trim());
                int stockQty = Integer.parseInt(StockQty.getText().trim());
                String category = Category.getValue();
                LocalDate dateAdded = LastUpdated.getValue();
                String dateAddedStr = dateAdded != null ? dateAdded.toString() : LocalDate.now().toString();

                Part newPart = new Part(partCode, description, brand, price, stockQty, category, dateAddedStr, "");

                if (parentController != null) {
                    parentController.addNewPart(newPart);
                }

                showSuccessAlert("Success", "Part added successfully!");
                closeWindow();
            } catch (NumberFormatException e) {
                showErrorAlert("Invalid Input", "Price must be a valid number and Stock Qty must be an integer.");
            }
        }
    }

    private boolean validateInput() {
        if (PartCode.getText().trim().isEmpty()) {
            showErrorAlert("Validation Error", "Part Code is required.");
            return false;
        }
        if (Description.getText().trim().isEmpty()) {
            showErrorAlert("Validation Error", "Description is required.");
            return false;
        }
        if (Brand.getText().trim().isEmpty()) {
            showErrorAlert("Validation Error", "Brand is required.");
            return false;
        }
        if (Price.getText().trim().isEmpty()) {
            showErrorAlert("Validation Error", "Price is required.");
            return false;
        }
        if (StockQty.getText().trim().isEmpty()) {
            showErrorAlert("Validation Error", "Stock Qty is required.");
            return false;
        }
        if (Category.getValue() == null || Category.getValue().isEmpty()) {
            showErrorAlert("Validation Error", "Please select a category.");
            return false;
        }
        if (LastUpdated.getValue() == null) {
            showErrorAlert("Validation Error", "Last Updated date is required.");
            return false;
        }
        return true;
    }

    private void closeWindow() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}