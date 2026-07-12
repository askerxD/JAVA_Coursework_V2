package com.example.javacw;

import com.example.javacw.objects.Part;
import com.example.javacw.parsers.InventoryParser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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

     @Override
     public void initialize(URL url, ResourceBundle resourceBundle) {
          loadInventoryData();
     }

     private void loadInventoryData() {
          String filePath = "src/main/java/com/example/javacw/data/inventory_legacy.txt";
          ArrayList<Part> parts = InventoryParser.parseInventoryFile(filePath);

          setupTableColumns();
          inventoryTable.getItems().addAll(parts);
          
          updateInventoryStats(parts);
     }

     private void updateInventoryStats(ArrayList<Part> parts) {
          // Task 2: Update parts count
          partsCount.setText(String.valueOf(parts.size()));
          
          // Task 3: Calculate and update total inventory value
          double totalValue = 0.0;
          for (Part part : parts) {
               totalValue += part.getPrice() * part.getQuantity();
          }
          totalnventoryValue.setText(String.format("Rs. %.2f", totalValue));
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
}
