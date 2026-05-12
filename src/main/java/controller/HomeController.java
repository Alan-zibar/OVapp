package controller;
import Service.RouteService;

import Service.NavigationService;
import Service.TripService;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.util.Duration;

import model.Route;
import model.TransportType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class HomeController {

    @FXML
    private ComboBox<String> fromComboBox;

    @FXML
    private ComboBox<String> toComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label messageLabel;

    @FXML
    private Label currentTimeLabel;

    private final TripService tripService = new TripService();
    private final RouteService routeService = new RouteService();
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Timeline clockTimeline;

    @FXML
    private void initialize() {
        fromComboBox.getItems().addAll(tripService.getStations());
        toComboBox.getItems().addAll(tripService.getStations());
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

        fromComboBox.setValue(to);
        toComboBox.setValue(from);
    }


    @FXML
    private void searchTrip(ActionEvent event) {
        System.out.println("searchTip called");
        String from = fromComboBox.getValue();
        String to = toComboBox.getValue();
        System.out.println("from=" + from + ", to=" + to);

        if (from == null || to == null) {
            messageLabel.setText("Kies vertrek en aankomst.");
            return;
        }

        TransportType type = TransportType.TREIN;
        Route route = routeService.findRoute(from, to, type);

        if (route == null) {
            messageLabel.setText("Geen route gevonden.");
            return;
        }

        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText(String.format("Route: %s → %s, %d min, %d km",
                route.getFrom(), route.getTo(),
                route.getDurationMinutes(), route.getDistanceKm()));

    }

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
