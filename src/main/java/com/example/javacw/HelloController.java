package com.example.javacw;

import com.example.javacw.objects.Part;
import com.example.javacw.service.AuditService;
import com.example.javacw.service.InventoryService;
import com.example.javacw.utils.LowStockUtil;
import com.example.javacw.utils.SceneNavigationUtil;
import com.example.javacw.utils.SearchUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class HelloController implements Initializable {
     private static final String INVENTORY_PATH = "src/main/java/com/example/javacw/data/inventory_legacy.txt";

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
     private TableView<Part> inventoryTable;
     @FXML
     private Label totalnventoryValue;
     @FXML
     private Label partsCount;
     @FXML
     private VBox lowStockWarning;
     @FXML
     private ChoiceBox<String> catogoryFilterChoiceBox;
     @FXML
     private Button search;
     @FXML
     private Button resetSearch;

     private InventoryService inventoryService;
     private int currentThreshold = 4;
     private final AuditService auditService = AuditService.getDefault();

     @Override
     public void initialize(URL url, ResourceBundle resourceBundle) {
          loadInventoryData();
          setupLowStockThresholdButton();
          setupSearchButtons();
          setupAddNewPartButton();
          setupUpdateSelectedButton();
          setupDeleteSelectedButton();
          setupNavigationButtons();
          populateCategories();
     }

     private void loadInventoryData() {
          inventoryService = new InventoryService(INVENTORY_PATH);
          setupTableColumns();
          refreshTableFromService();
          lowStockThreshold.setText(String.valueOf(currentThreshold));
     }

     private void refreshTableFromService() {
          ArrayList<Part> parts = inventoryService.getAllParts();
          inventoryTable.getItems().clear();
          inventoryTable.getItems().addAll(parts);
          updateInventoryStats(parts);
          displayLowStockWarnings(currentThreshold);
     }

     private void populateCategories() {
          Set<String> uniqueCategories = new HashSet<>();
          for (Part part : inventoryService.getAllParts()) {
               uniqueCategories.add(part.getCategory());
          }

          catogoryFilterChoiceBox.getItems().clear();
          catogoryFilterChoiceBox.getItems().add("All Categories");
          catogoryFilterChoiceBox.getItems().addAll(uniqueCategories);
          catogoryFilterChoiceBox.setValue("All Categories");
     }

     private void setupSearchButtons() {
          search.setOnAction(event -> performSearch());
          resetSearch.setOnAction(event -> resetFilters());
     }

     private void setupNavigationButtons() {
          SceneNavigationUtil.setupNavigationButtons(
                  inventoryDashboardButton,
                  posCheckoutButton,
                  dealerSelectionButton,
                  auditLoButton
          );
     }

     private void setupAddNewPartButton() {
          addNewPartButton.setOnAction(event -> openAddItemWindow());
     }

     private void setupUpdateSelectedButton() {
          if (updateSelectedButton != null) {
               updateSelectedButton.setOnAction(event -> openUpdateItemWindow());
          }
     }

     private void setupDeleteSelectedButton() {
          if (deleteSelectedButton != null) {
               deleteSelectedButton.setOnAction(event -> handleDeleteSelected());
          }
     }

     private void handleDeleteSelected() {
          Part selectedPart = inventoryTable.getSelectionModel().getSelectedItem();

          if (selectedPart == null) {
               showErrorAlert("No Selection", "Please select a part to delete.");
               return;
          }

          Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
          confirmAlert.setTitle("Confirm Delete");
          confirmAlert.setHeaderText("Delete Part?");
          confirmAlert.setContentText("Are you sure you want to delete '" + selectedPart.getPartCode()
                  + " - " + selectedPart.getName() + "'? This action cannot be undone.");

          if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
               String partCode = selectedPart.getPartCode();
               if (inventoryService.deletePart(partCode)) {
                    auditService.logInventoryDeleteAsAdmin(partCode);
                    resetFilters();
                    populateCategories();
                    showSuccessAlert("Success", "Part deleted successfully!");
               } else {
                    showErrorAlert("Delete Failed", "Could not delete the selected part.");
               }
          }
     }

     private void showSuccessAlert(String title, String message) {
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle(title);
          alert.setContentText(message);
          alert.showAndWait();
     }

     private void openAddItemWindow() {
          try {
               FXMLLoader loader = new FXMLLoader(getClass().getResource("addItemScene.fxml"));
               Scene scene = new Scene(loader.load(), 600, 400);

               ANPController controller = loader.getController();
               controller.setParentController(this);

               Stage stage = new Stage();
               stage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
               stage.setTitle("Add New Part");
               stage.setScene(scene);
               stage.show();
          } catch (IOException e) {
               e.printStackTrace();
               showErrorAlert("Error", "Failed to open Add Item window");
          }
     }

     private void openUpdateItemWindow() {
          Part selectedPart = inventoryTable.getSelectionModel().getSelectedItem();

          if (selectedPart == null) {
               showErrorAlert("No Selection", "Please select a part to update.");
               return;
          }

          try {
               FXMLLoader loader = new FXMLLoader(getClass().getResource("updateItemScene.fxml"));
               Scene scene = new Scene(loader.load(), 600, 350);

               UPDController controller = loader.getController();
               controller.setParentController(this);
               controller.setPart(selectedPart);

               Stage stage = new Stage();
               stage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
               stage.setTitle("Update Part");
               stage.setScene(scene);
               stage.show();
          } catch (IOException e) {
               e.printStackTrace();
               showErrorAlert("Error", "Failed to open Update Item window: " + e.getMessage());
          } catch (Exception e) {
               e.printStackTrace();
               showErrorAlert("Error", "An unexpected error occurred: " + e.getMessage());
          }
     }

     private void showErrorAlert(String title, String message) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle(title);
          alert.setContentText(message);
          alert.showAndWait();
     }

     private void performSearch() {
          String keyword = keyworkSearchField.getText();
          String category = catogoryFilterChoiceBox.getValue();
          String minPrice = minPriceField.getText();
          String maxPrice = maxPriceField.getText();

          if (category != null && category.equals("All Categories")) {
               category = "";
          }

          if (minPrice == null || minPrice.trim().isEmpty()) {
               minPrice = "0";
          }
          if (maxPrice == null || maxPrice.trim().isEmpty()) {
               maxPrice = "999999";
          }

          ArrayList<Part> filteredParts = SearchUtil.filterParts(
                  inventoryService.getAllParts(), keyword, category, minPrice, maxPrice);

          inventoryTable.getItems().clear();
          inventoryTable.getItems().addAll(filteredParts);
          updateInventoryStats(filteredParts);
     }

     private void resetFilters() {
          keyworkSearchField.clear();
          catogoryFilterChoiceBox.setValue("All Categories");
          minPriceField.clear();
          maxPriceField.clear();
          refreshTableFromService();
     }

     private void updateInventoryStats(ArrayList<Part> parts) {
          partsCount.setText(String.valueOf(parts.size()));

          double totalValue = 0.0;
          for (Part part : parts) {
               totalValue += part.getPrice() * part.getQuantity();
          }
          totalnventoryValue.setText(String.format("Rs. %.2f", totalValue));
     }

     private void setupLowStockThresholdButton() {
          lowStockThresholdSave.setOnAction(event -> {
               try {
                    int newThreshold = Integer.parseInt(lowStockThreshold.getText().trim());
                    if (newThreshold >= 0) {
                         int oldThreshold = currentThreshold;
                         currentThreshold = newThreshold;
                         displayLowStockWarnings(currentThreshold);
                         if (oldThreshold != newThreshold) {
                              auditService.logLowStockThresholdChangeAsManager(oldThreshold, newThreshold);
                         }
                    } else {
                         showErrorAlert("Invalid Threshold", "Threshold must be 0 or greater.");
                         lowStockThreshold.setText(String.valueOf(currentThreshold));
                    }
               } catch (NumberFormatException e) {
                    showErrorAlert("Invalid Threshold", "Please enter a whole number for the threshold.");
                    lowStockThreshold.setText(String.valueOf(currentThreshold));
               }
          });
     }

     private void displayLowStockWarnings(int threshold) {
          lowStockWarning.getChildren().clear();

          ArrayList<Part> lowStockItems = LowStockUtil.getLowStockItems(
                  inventoryService.getAllParts(), threshold);

          for (Part part : lowStockItems) {
               Label warningLabel = new Label();
               String text = part.getPartCode() + " - " + part.getName() + "\n" +
                       "remaining | " + part.getQuantity();
               warningLabel.setText(text);
               warningLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 14;");
               warningLabel.setWrapText(true);
               lowStockWarning.getChildren().add(warningLabel);
          }
     }

     private void setupTableColumns() {
          TableColumn<Part, String> partCodeCol = (TableColumn<Part, String>) inventoryTable.getColumns().get(0);
          partCodeCol.setCellValueFactory(new PropertyValueFactory<>("partCode"));

          TableColumn<Part, String> descriptionCol = (TableColumn<Part, String>) inventoryTable.getColumns().get(1);
          descriptionCol.setCellValueFactory(new PropertyValueFactory<>("name"));

          TableColumn<Part, String> brandCol = (TableColumn<Part, String>) inventoryTable.getColumns().get(2);
          brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));

          TableColumn<Part, Double> priceCol = (TableColumn<Part, Double>) inventoryTable.getColumns().get(3);
          priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

          TableColumn<Part, Integer> qtyCol = (TableColumn<Part, Integer>) inventoryTable.getColumns().get(4);
          qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

          TableColumn<Part, String> categoryCol = (TableColumn<Part, String>) inventoryTable.getColumns().get(5);
          categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

          TableColumn<Part, String> lastUpdatedCol = (TableColumn<Part, String>) inventoryTable.getColumns().get(6);
          lastUpdatedCol.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
     }

     public boolean partCodeExists(String partCode) {
          return inventoryService.partCodeExists(partCode);
     }

     /**
      * @return true if the part was added and saved; false if duplicate part code
      */
     public boolean addNewPart(Part newPart) {
          if (newPart == null) {
               return false;
          }
          if (!inventoryService.addPart(newPart)) {
               return false;
          }
          auditService.logInventoryAddAsAdmin(newPart.getPartCode(), newPart.getQuantity());
          populateCategories();
          resetFilters();
          return true;
     }

     public void refreshInventoryTable() {
          inventoryService.persistAndResort();
          populateCategories();
          resetFilters();
     }
}
