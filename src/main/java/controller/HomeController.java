package controller;

import Service.LanguageService;
import Service.NavigationService;
import Service.TripService;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.util.Duration;

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

    @FXML
    private Label currentTimeTextLabel;

    @FXML
    private Label menuTitleLabel;

    @FXML
    private Label planTitleLabel;

    @FXML
    private Label fromTitleLabel;

    @FXML
    private Label toTitleLabel;

    @FXML
    private Label dateTimeLabel;

    @FXML
    private MenuButton languageMenuButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button homeMenuButton;

    @FXML
    private Button planTripMenuButton;

    @FXML
    private Button favoritesMenuButton;

    @FXML
    private Button historyMenuButton;

    @FXML
    private Button settingsMenuButton;

    @FXML
    private Button searchButton;

    private final TripService tripService = new TripService();
    private final LanguageService languageService = new LanguageService();
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Timeline clockTimeline;

    @FXML
    private void initialize() {
        fromComboBox.getItems().addAll(tripService.getStations());
        toComboBox.getItems().addAll(tripService.getStations());
        updateTexts();
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
    private void setDutchLanguage() {
        languageService.useDutch();
        updateTexts();
    }

    @FXML
    private void setEnglishLanguage() {
        languageService.useEnglish();
        updateTexts();
    }

    private void updateTexts() {
        languageMenuButton.setText(languageService.getLanguageCode());
        currentTimeTextLabel.setText(languageService.text("Huidige tijd:", "Current time:"));
        loginButton.setText(languageService.text("\uD83D\uDC64  Inloggen", "\uD83D\uDC64  Login"));

        menuTitleLabel.setText("Menu");
        homeMenuButton.setText("\u2302  Home");
        planTripMenuButton.setText(languageService.text("\uD83D\uDE89  Reis plannen", "\uD83D\uDE89  Plan trip"));
        favoritesMenuButton.setText(languageService.text("\u2606  Favorieten", "\u2606  Favorites"));
        historyMenuButton.setText(languageService.text("\u25F7  Geschiedenis", "\u25F7  History"));
        settingsMenuButton.setText(languageService.text("\u2699  Instellingen", "\u2699  Settings"));

        planTitleLabel.setText(languageService.text("Plan je reis", "Plan your trip"));
        fromTitleLabel.setText(languageService.text("Van", "From"));
        toTitleLabel.setText(languageService.text("Naar", "To"));
        dateTimeLabel.setText(languageService.text("Datum en tijd", "Date and time"));
        searchButton.setText(languageService.text("\uD83D\uDD0D  Zoek reis", "\uD83D\uDD0D  Search trip"));

        fromComboBox.setPromptText(languageService.text("Kies vertrekstation", "Choose departure station"));
        toComboBox.setPromptText(languageService.text("Kies aankomststation", "Choose arrival station"));

        if (messageLabel.getText() != null && !messageLabel.getText().isEmpty()) {
            messageLabel.setText(getChooseStationsMessage());
        }
    }

    private String getChooseStationsMessage() {
        return languageService.text(
                "Kies een vertrekstation en aankomststation.",
                "Choose a departure station and arrival station."
        );
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
            messageLabel.setText(getChooseStationsMessage());
            return;
        }

        NavigationService.switchScene(event, "results.fxml");
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
