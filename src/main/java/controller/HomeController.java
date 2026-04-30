package controller;

import Service.NavigationService;
import Service.TripService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class HomeController {

    @FXML
    private ComboBox<String> fromComboBox;

    @FXML
    private ComboBox<String> toComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label messageLabel;

    private final TripService tripService = new TripService();

    @FXML
    private void initialize() {
        fromComboBox.getItems().addAll(tripService.getStations());
        toComboBox.getItems().addAll(tripService.getStations());
    }

    @FXML
    private void swapStations() {
        String from = fromComboBox.getValue();
        String to = toComboBox.getValue();

        fromComboBox.setValue(to);
        toComboBox.setValue(from);
    }

    @FXML
    private void searchTrip(ActionEvent event) {
        String from = fromComboBox.getValue();
        String to = toComboBox.getValue();

        if (!tripService.isValidTrip(from, to)) {
            messageLabel.setText("Kies een vertrekstation en aankomststation.");
            return;
        }

        NavigationService.switchScene(event, "results.fxml");
    }

    @FXML
    private void goToHome(ActionEvent event) {
        NavigationService.switchScene(event, "home.fxml");
    }

    @FXML
    private void goToFavorites(ActionEvent event) {
        NavigationService.switchScene(event, "favorites.fxml");
    }

    @FXML
    private void goToHistory(ActionEvent event) {
        NavigationService.switchScene(event, "history.fxml");
    }

    @FXML
    private void goToSettings(ActionEvent event) {
        NavigationService.switchScene(event, "settings.fxml");
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        NavigationService.switchScene(event, "login.fxml");
    }
}