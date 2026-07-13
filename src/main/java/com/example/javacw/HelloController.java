package com.example.javacw;

import com.example.javacw.objects.Part;
import com.example.javacw.parsers.InventoryParser;
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

     private ArrayList<Part> allParts;
     private int currentThreshold = 4;

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
          String filePath = "src/main/java/com/example/javacw/data/inventory_legacy.txt";
          allParts = InventoryParser.parseInventoryFile(filePath);

          setupTableColumns();
          inventoryTable.getItems().addAll(allParts);
          
          updateInventoryStats(allParts);
          displayLowStockWarnings(currentThreshold);
          lowStockThreshold.setText(String.valueOf(currentThreshold));
     }

     private void populateCategories() {
          Set<String> uniqueCategories = new HashSet<>();
          for (Part part : allParts) {
               uniqueCategories.add(part.getCategory());
          }
          
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
               System.out.println("Update Selected Button handler registered successfully");
          } else {
               System.out.println("ERROR: updateSelectedButton is NULL - not properly injected from FXML");
          }
     }

     private void setupDeleteSelectedButton() {
          if (deleteSelectedButton != null) {
               deleteSelectedButton.setOnAction(event -> handleDeleteSelected());
               System.out.println("Delete Selected Button handler registered successfully");
          } else {
               System.out.println("ERROR: deleteSelectedButton is NULL - not properly injected from FXML");
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
          confirmAlert.setContentText("Are you sure you want to delete '" + selectedPart.getPartCode() + " - " + selectedPart.getName() + "'? This action cannot be undone.");
           
          if (confirmAlert.showAndWait().get() == ButtonType.OK) {
               allParts.remove(selectedPart);
               inventoryTable.getItems().remove(selectedPart);
               updateInventoryStats(allParts);
               displayLowStockWarnings(currentThreshold);
               showSuccessAlert("Success", "Part deleted successfully!");
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
          
          // Handle "All Categories" selection
          if (category != null && category.equals("All Categories")) {
               category = "";
          }
          
          // Set default values for empty price fields
          if (minPrice == null || minPrice.trim().isEmpty()) {
               minPrice = "0";
          }
          if (maxPrice == null || maxPrice.trim().isEmpty()) {
               maxPrice = "999999";
          }
          
          ArrayList<Part> filteredParts = SearchUtil.filterParts(allParts, keyword, category, minPrice, maxPrice);
          
          inventoryTable.getItems().clear();
          inventoryTable.getItems().addAll(filteredParts);
          updateInventoryStats(filteredParts);
     }

     private void resetFilters() {
          keyworkSearchField.clear();
          catogoryFilterChoiceBox.setValue("All Categories");
          minPriceField.clear();
          maxPriceField.clear();
          
          inventoryTable.getItems().clear();
          inventoryTable.getItems().addAll(allParts);
          updateInventoryStats(allParts);
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
                    int newThreshold = Integer.parseInt(lowStockThreshold.getText());
                    if (newThreshold >= 0) {
                         currentThreshold = newThreshold;
                         displayLowStockWarnings(currentThreshold);
                    }
               } catch (NumberFormatException e) {
                    lowStockThreshold.setText(String.valueOf(currentThreshold));
               }
          });
     }

     private void displayLowStockWarnings(int threshold) {
          lowStockWarning.getChildren().clear();
          
          ArrayList<Part> lowStockItems = LowStockUtil.getLowStockItems(allParts, threshold);
          
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

     public void addNewPart(Part newPart) {
          allParts.add(newPart);
          inventoryTable.getItems().add(newPart);
          updateInventoryStats(allParts);
          displayLowStockWarnings(currentThreshold);
     }

     public void refreshInventoryTable() {
          inventoryTable.refresh();
          updateInventoryStats(allParts);
          displayLowStockWarnings(currentThreshold);
     }
}
