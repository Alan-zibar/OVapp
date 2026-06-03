package controller;

import Service.FavoriteService;
import Service.LanguageService;
import Service.SessionService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Trip;

public class ResultCardController {

    @FXML
    private Label transportIconLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label routeLabel;
    @FXML
    private Label transportLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label arrivalLabel;
    @FXML
    private Button favoriteButton;
    @FXML
    private Button viewRouteButton;
    @FXML
    private HBox favoriteMessageBox;
    @FXML
    private Label favoriteMessageLabel;
    @FXML
    private VBox routeDetails;
    @FXML
    private RouteDetailsController routeDetailsController;

    private Trip trip;

    public void setTrip(Trip trip) {
        this.trip = trip;
        refreshContent();
        routeDetailsController.setTrip(trip);
    }

    private void refreshContent() {
        boolean isBus = "BUS".equals(trip.getTransportMode());

        transportIconLabel.setText(isBus ? "\uD83D\uDE8C" : "\uD83D\uDE86");
        timeLabel.setText(trip.getDepartureTime() + "  \u2192  " + trip.getScheduledArrivalTime());
        routeLabel.setText(trip.getFromStation() + "  \u2192  " + trip.getToStation());
        transportLabel.setText(transportSummary());
        statusLabel.setText(statusText());
        arrivalLabel.setText(LanguageService.text(
                "Verwachte aankomst: " + trip.getExpectedArrivalTime(),
                "Expected arrival: " + trip.getExpectedArrivalTime()
        ));
        favoriteButton.setText(FavoriteService.isFavorite(trip) ? "\u2605" : "\u2606");
        viewRouteButton.setText(LanguageService.text("Bekijk route", "View route"));
    }

    @FXML
    private void addToFavorites() {
        if (!SessionService.isLoggedIn()) {
            showFavoriteMessage(LanguageService.text(
                    "Log eerst in om favorieten op te slaan",
                    "Please log in first to save favorites"
            ));
            return;
        }

        if (FavoriteService.isFavorite(trip)) {
            showFavoriteMessage(LanguageService.text(
                    "Deze route staat al in je favorieten",
                    "This route is already in your favorites"
            ));
            return;
        }

        FavoriteService.addFavorite(trip);
        favoriteButton.setText("\u2605");
        showFavoriteMessage(LanguageService.text(
                "Toegevoegd als favorieten",
                "Saved as favorites"
        ));
    }

    @FXML
    private void closeFavoriteMessage() {
        favoriteMessageBox.setVisible(false);
        favoriteMessageBox.setManaged(false);
    }

    @FXML
    private void toggleRouteDetails() {
        boolean showDetails = !routeDetails.isVisible();
        routeDetails.setVisible(showDetails);
        routeDetails.setManaged(showDetails);
        viewRouteButton.setText(showDetails
                ? LanguageService.text("Sluit route", "Close route")
                : LanguageService.text("Bekijk route", "View route"));
    }

    private void showFavoriteMessage(String message) {
        favoriteMessageLabel.setText(message);
        favoriteMessageBox.setVisible(true);
        favoriteMessageBox.setManaged(true);
    }

    private String statusText() {
        if (trip.getDelayMinutes() <= 0) {
            return LanguageService.text("Op tijd", "On time");
        }

        return LanguageService.text(
                "+" + trip.getDelayMinutes() + " min vertraging",
                "+" + trip.getDelayMinutes() + " min delay"
        );
    }

    private String transportSummary() {
        String transportName = "BUS".equals(trip.getTransportMode())
                ? LanguageService.text("Bus", "Bus")
                : LanguageService.text("Trein", "Train");

        return transportName + " | " + trip.getTransportType() + " | " + localizedLocation(trip.getDepartureLocation());
    }

    private String localizedLocation(String location) {
        if (location.startsWith("Spoor ")) {
            return LanguageService.text(location, "Platform " + location.substring("Spoor ".length()));
        }

        if (location.startsWith("Bushalte ")) {
            return LanguageService.text(location, "Bus stop " + location.substring("Bushalte ".length()));
        }

        return location;
    }
}
