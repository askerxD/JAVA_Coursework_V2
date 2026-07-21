package com.example.javacw;

import com.example.javacw.objects.Part;
import com.example.javacw.service.AuditService;
import com.example.javacw.utils.ValidationUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class UPDController implements Initializable {
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
    @FXML
    private Button updateImage;
    @FXML
    private TextField lsThreshold;
    @FXML
    private ImageView partImage;



    private HelloController parentController;
    private Part selectedPart;
    private String originalPartCode;
    private final AuditService auditService = AuditService.getDefault();

    public void setParentController(HelloController controller) {
        this.parentController = controller;
    }

    public void setPart(Part part) {
        this.selectedPart = part;
        this.originalPartCode = part != null ? part.getPartCode() : null;
        populateFields(part);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupCategoryDropdown();
        setupButtonHandlers();
    }

    private void setupCategoryDropdown() {
        Category.getItems().addAll("ENGINE", "ELECTRICAL", "BRAKES", "BODYWORK");
    }

    private void setupButtonHandlers() {
        cancel.setOnAction(event -> handleCancel());
        SavePart.setOnAction(event -> handleSavePart());
    }

    private void populateFields(Part part) {
        if (part != null) {
            PartCode.setText(part.getPartCode());
            Description.setText(part.getName());
            Brand.setText(part.getBrand());
            Price.setText(String.valueOf(part.getPrice()));
            StockQty.setText(String.valueOf(part.getQuantity()));
            Category.setValue(ValidationUtil.normalizeCategory(part.getCategory()));

            try {
                String dateStr = part.getDateAdded();
                LocalDate date;

                try {
                    date = LocalDate.parse(dateStr);
                } catch (Exception e1) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        date = LocalDate.parse(dateStr, formatter);
                    } catch (Exception e2) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        date = LocalDate.parse(dateStr, formatter);
                    }
                }
                LastUpdated.setValue(date);
            } catch (Exception e) {
                System.out.println("Error parsing date: " + part.getDateAdded());
                LastUpdated.setValue(LocalDate.now());
            }
        }
    }

    private void handleCancel() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Cancel");
        confirmAlert.setHeaderText("Cancel Operation?");
        confirmAlert.setContentText("Are you sure you want to cancel? Any unsaved changes will be lost.");

        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            closeWindow();
        }
    }

    private void handleSavePart() {
        if (!validateInput()) {
            return;
        }
        try {
            String partCode = PartCode.getText().trim().toUpperCase();
            String description = Description.getText().trim();
            String brand = Brand.getText().trim();
            double price = Double.parseDouble(Price.getText().trim());
            int stockQty = Integer.parseInt(StockQty.getText().trim());
            String category = ValidationUtil.normalizeCategory(Category.getValue());
            LocalDate dateAdded = LastUpdated.getValue();
            String dateAddedStr = dateAdded != null
                    ? dateAdded.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            if (!ValidationUtil.isNonNegativePrice(price)) {
                showErrorAlert("Validation Error", "Price cannot be negative.");
                return;
            }
            if (!ValidationUtil.isNonNegativeQuantity(stockQty)) {
                showErrorAlert("Validation Error", "Stock quantity cannot be negative.");
                return;
            }

            if (parentController != null
                    && !partCode.equalsIgnoreCase(originalPartCode)
                    && parentController.partCodeExists(partCode)) {
                showErrorAlert("Duplicate Part Code",
                        "Part code '" + partCode + "' already exists. Please use a unique code.");
                return;
            }

            if (selectedPart != null) {
                String before = "name=" + safe(selectedPart.getName()) +
                        ", brand=" + safe(selectedPart.getBrand()) +
                        ", price=" + selectedPart.getPrice() +
                        ", qty=" + selectedPart.getQuantity() +
                        ", cat=" + safe(selectedPart.getCategory()) +
                        ", date=" + safe(selectedPart.getDateAdded());

                selectedPart.setPartCode(partCode);
                selectedPart.setName(description);
                selectedPart.setBrand(brand);
                selectedPart.setPrice(price);
                selectedPart.setQuantity(stockQty);
                selectedPart.setCategory(category);
                selectedPart.setDateAdded(dateAddedStr);

                if (parentController != null) {
                    parentController.refreshInventoryTable();
                }

                String after = "name=" + safe(description) +
                        ", brand=" + safe(brand) +
                        ", price=" + price +
                        ", qty=" + stockQty +
                        ", cat=" + safe(category) +
                        ", date=" + safe(dateAddedStr);

                auditService.logInventoryUpdateAsAdmin(partCode, before + " -> " + after);
            }

            showSuccessAlert("Success", "Part updated successfully!");
            closeWindow();
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid Input", "Price must be a valid number and Stock Qty must be an integer.");
        }
    }

    private boolean validateInput() {
        String partCode = PartCode.getText().trim().toUpperCase();
        if (partCode.isEmpty()) {
            showErrorAlert("Validation Error", "Part Code is required.");
            return false;
        }
        if (!ValidationUtil.isValidPartCode(partCode)) {
            showErrorAlert("Validation Error", "Part Code must match format P followed by digits (e.g. P011).");
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

    private String safe(String value) {
        if (value == null) return "";
        return value.trim();
    }
}