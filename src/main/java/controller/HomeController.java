package controller;

import Service.NavigationService;
import Service.TripService;
import Service.RouteService;
import model.Route;
import model.TransportType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomeController {

    @FXML private ComboBox<String> fromComboBox;
    @FXML private ComboBox<String> toComboBox;
    @FXML private DatePicker datePicker;
    @FXML private Label messageLabel;
    @FXML private Label currentTimeLabel;

    private final TripService tripService = new TripService();
    private final RouteService routeService = new RouteService();
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Timeline clockTimeline;

    @FXML
    private void initialize() {
        List<String> stations = routeService.getAllStations();
        fromComboBox.getItems().addAll(stations);
        toComboBox.getItems().addAll(stations);

        if (!stations.isEmpty()) {
            fromComboBox.setValue(stations.get(0));
            if (stations.size() > 1) toComboBox.setValue(stations.get(1));
        }

        startClock();
    }

    private void startClock() {
        updateCurrentTime();
        clockTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateCurrentTime())
        );
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
    }

    private void updateCurrentTime() {
        currentTimeLabel.setText(LocalTime.now().format(timeFormatter));
    }

    @FXML
    private void swapStations() {
        String from = fromComboBox.getValue();
        String to = toComboBox.getValue();
        if (from != null && to != null) {
            fromComboBox.setValue(to);
            toComboBox.setValue(from);
        }
    }

    @FXML
    private void searchTrip(ActionEvent event) {
        String from = fromComboBox.getValue();
        String to = toComboBox.getValue();

        if (from == null || to == null) {
            messageLabel.setText("Kies eerst vertrek en aankomst.");
            return;
        }

        TransportType type = TransportType.TREIN;
        Route route = routeService.findRoute(from, to, type);

        if (route == null) {
            messageLabel.setText("Geen route gevonden tussen " + from + " en " + to);
            return;
        }

        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText(String.format("Route: %s → %s, %d min, %d km",
                route.getFrom(), route.getTo(),
                route.getDurationMinutes(), route.getDistanceKm()));
    }

    // Navigatiemethoden
    @FXML
    private void goToHome(ActionEvent event) {
        NavigationService.switchScene(event, "home.fxml");
    }

    @FXML
    private void goToPlanTrip(ActionEvent event) {
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