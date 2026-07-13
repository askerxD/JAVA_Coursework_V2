package com.example.javacw;

import com.example.javacw.objects.Dealer;
import com.example.javacw.service.DealerService;
import com.example.javacw.utils.SceneNavigationUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DealerSelectionController implements Initializable {
    @FXML
    private Button inventoryDashboardButton;
    @FXML
    private Button posCheckoutButton;
    @FXML
    private Button dealerSelectionButton;
    @FXML
    private Button auditLoButton;
    @FXML
    private Label dealerID1;
    @FXML
    private Label dealerContact1;
    @FXML
    private Label dealerLocation1;
    @FXML
    private Label dealerID2;
    @FXML
    private Label dealerContact2;
    @FXML
    private Label dealerLocation2;
    @FXML
    private Label dealerID3;
    @FXML
    private Label dealerContact3;
    @FXML
    private Label dealerLocation3;
    @FXML
    private Label dealerID4;
    @FXML
    private Label dealerContact4;
    @FXML
    private Label dealerLocation4;
    @FXML
    private Button refresh;

    private DealerService dealerService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SceneNavigationUtil.setupNavigationButtons(
                inventoryDashboardButton,
                posCheckoutButton,
                dealerSelectionButton,
                auditLoButton
        );

        dealerService = new DealerService("src/main/java/com/example/javacw/data/dealers_legacy.txt");

        if (refresh != null) {
            refresh.setOnAction(event -> loadRandomDealersIntoLabels());
        }

        loadRandomDealersIntoLabels();
    }

    private void loadRandomDealersIntoLabels() {
        if (dealerService == null) {
            return;
        }

        ArrayList<Dealer> selected = dealerService.getRandomFourDealers();

        setDealerLabels(selected, 0, dealerID1, dealerContact1, dealerLocation1);
        setDealerLabels(selected, 1, dealerID2, dealerContact2, dealerLocation2);
        setDealerLabels(selected, 2, dealerID3, dealerContact3, dealerLocation3);
        setDealerLabels(selected, 3, dealerID4, dealerContact4, dealerLocation4);
    }

    private void setDealerLabels(ArrayList<Dealer> selected,
                                 int index,
                                 Label idLabel,
                                 Label contactLabel,
                                 Label locationLabel) {
        if (idLabel == null || contactLabel == null || locationLabel == null) {
            return;
        }

        if (selected == null || index >= selected.size()) {
            idLabel.setText("-");
            contactLabel.setText("-");
            locationLabel.setText("-");
            return;
        }

        Dealer dealer = selected.get(index);
        idLabel.setText(nullToDash(dealer.getDealerId()));
        contactLabel.setText(nullToDash(dealer.getContactNumber()));
        locationLabel.setText(nullToDash(dealer.getLocation()));
    }

    private String nullToDash(String value) {
        if (value == null) {
            return "-";
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? "-" : trimmed;
    }
}
