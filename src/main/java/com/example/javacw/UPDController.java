package com.example.javacw;

import com.example.javacw.objects.Part;
import com.example.javacw.service.AuditService;
import com.example.javacw.utils.ValidationUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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
    private Button updateImage; // This button is not used in the current logic, but kept for FXML consistency
    @FXML
    private TextField lsThreshold;
    @FXML
    private ImageView partImage;
    @FXML
    private TextField image;


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
        // Add listener to image TextField to update ImageView dynamically
        image.textProperty().addListener((observable, oldValue, newValue) -> updateImageView(newValue));
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
            lsThreshold.setText(String.valueOf(part.getLowStockThreshold()));

            // Populate image TextField
            image.setText(part.getImage());
            // Display the image based on the current part's image name
            updateImageView(part.getImage());


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
            int lowStockThresholdValue = Integer.parseInt(lsThreshold.getText().trim());
            String imageName = image.getText().trim(); // Get image name from TextField

            if (!ValidationUtil.isNonNegativePrice(price)) {
                showErrorAlert("Validation Error", "Price cannot be negative.");
                return;
            }
            if (!ValidationUtil.isNonNegativeQuantity(stockQty)) {
                showErrorAlert("Validation Error", "Stock quantity cannot be negative.");
                return;
            }
            if (lowStockThresholdValue < 0) {
                showErrorAlert("Validation Error", "Low stock threshold cannot be negative.");
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
                        ", date=" + safe(selectedPart.getDateAdded()) +
                        ", threshold=" + selectedPart.getLowStockThreshold() +
                        ", image=" + safe(selectedPart.getImage());

                selectedPart.setPartCode(partCode);
                selectedPart.setName(description);
                selectedPart.setBrand(brand);
                selectedPart.setPrice(price);
                selectedPart.setQuantity(stockQty);
                selectedPart.setCategory(category);
                selectedPart.setDateAdded(dateAddedStr);
                selectedPart.setLowStockThreshold(lowStockThresholdValue);
                selectedPart.setImage(imageName); // Set the new image name

                if (parentController != null) {
                    parentController.refreshInventoryTable();
                }

                String after = "name=" + safe(description) +
                        ", brand=" + safe(brand) +
                        ", price=" + price +
                        ", qty=" + stockQty +
                        ", cat=" + safe(category) +
                        ", date=" + safe(dateAddedStr) +
                        ", threshold=" + lowStockThresholdValue +
                        ", image=" + safe(selectedPart.getImage()); // Use updated image name here

                auditService.logInventoryUpdateAsAdmin(partCode, before + " -> " + after);
            }

            showSuccessAlert("Success", "Part updated successfully!");
            closeWindow();
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid Input", "Price, Stock Qty, and Low Stock Threshold must be valid numbers.");
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
        try {
            double price = Double.parseDouble(Price.getText().trim());
            if (!ValidationUtil.isNonNegativePrice(price)) {
                showErrorAlert("Validation Error", "Price cannot be negative.");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Validation Error", "Price must be a valid number.");
            return false;
        }

        if (StockQty.getText().trim().isEmpty()) {
            showErrorAlert("Validation Error", "Stock Qty is required.");
            return false;
        }
        try {
            int stockQty = Integer.parseInt(StockQty.getText().trim());
            if (!ValidationUtil.isNonNegativeQuantity(stockQty)) {
                showErrorAlert("Validation Error", "Stock quantity cannot be negative.");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Validation Error", "Stock Qty must be an integer.");
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

        // Validate Low Stock Threshold
        if (lsThreshold.getText().trim().isEmpty()) {
            showErrorAlert("Validation Error", "Low Stock Threshold is required.");
            return false;
        }
        try {
            int threshold = Integer.parseInt(lsThreshold.getText().trim());
            if (threshold < 0) {
                showErrorAlert("Validation Error", "Low Stock Threshold cannot be negative.");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Validation Error", "Low Stock Threshold must be an integer.");
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

    // New method to update the ImageView based on the provided image name
    private void updateImageView(String imageName) {
        if (imageName != null && !imageName.isEmpty()) {
            try {
                // The path for resources seems to be /com/example/javacw/ based on HelloController
                Image img = new Image(getClass().getResourceAsStream("/com/example/javacw/" + imageName));
                partImage.setImage(img);
                partImage.setFitHeight(100);
                partImage.setFitWidth(100);
            } catch (Exception e) {
                System.err.println("Error loading image in UPDController: " + imageName + " - " + e.getMessage());
                partImage.setImage(null); // Clear image if not found
            }
        } else {
            partImage.setImage(null); // Clear image if name is empty
        }
    }

    private String safe(String value) {
        if (value == null) return "";
        return value.trim();
    }
}