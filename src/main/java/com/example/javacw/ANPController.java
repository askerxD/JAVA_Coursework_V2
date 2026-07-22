package com.example.javacw;

import com.example.javacw.objects.Part;
import com.example.javacw.utils.ValidationUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

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
    @FXML
    private TextField lsThreshold;
    @FXML
    private TextField image;
    @FXML
    private Button addImage;
    @FXML
    private ImageView partImage; // Added ImageView


    private HelloController parentController;

    public void setParentController(HelloController controller) {
        this.parentController = controller;
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

    private void handleCancel() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Cancel");
        confirmAlert.setHeaderText("Cancel Operation?");
        confirmAlert.setContentText("Are you sure you want to cancel? Any unsaved data will be lost.");

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
                    ? dateAdded.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String imageName = image.getText().trim(); // Get image name from TextField

            if (!ValidationUtil.isNonNegativePrice(price)) {
                showErrorAlert("Validation Error", "Price cannot be negative.");
                return;
            }
            if (!ValidationUtil.isNonNegativeQuantity(stockQty)) {
                showErrorAlert("Validation Error", "Stock quantity cannot be negative.");
                return;
            }
            if (parentController != null && parentController.partCodeExists(partCode)) {
                showErrorAlert("Duplicate Part Code",
                        "Part code '" + partCode + "' already exists. Please use a unique code.");
                return;
            }

            Part newPart = new Part(partCode, description, brand, price, stockQty, category, dateAddedStr, imageName);

            if (parentController != null) {
                if (!parentController.addNewPart(newPart)) {
                    showErrorAlert("Duplicate Part Code",
                            "Part code '" + partCode + "' already exists. Please use a unique code.");
                    return;
                }
            }

            showSuccessAlert("Success", "Part added successfully!");
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
                System.err.println("Error loading image in ANPController: " + imageName + " - " + e.getMessage());
                partImage.setImage(null); // Clear image if not found
            }
        } else {
            partImage.setImage(null); // Clear image if name is empty
        }
    }
}