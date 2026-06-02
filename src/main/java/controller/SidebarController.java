package controller;

import Service.LanguageService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SidebarController {

    private static SidebarController instance;

    @FXML
    private Label menuTitleLabel;

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
    private void initialize() {
        instance = this;
        refreshLanguage();
    }

    public static void refreshSidebar() {
        if (instance != null) {
            instance.refreshLanguage();
        }
    }

    public void refreshLanguage() {
        menuTitleLabel.setText("Menu");
        homeMenuButton.setText("Home");
        planTripMenuButton.setText(LanguageService.text("Reis plannen", "Plan trip"));
        favoritesMenuButton.setText(LanguageService.text("Favorieten", "Favorites"));
        historyMenuButton.setText(LanguageService.text("Geschiedenis", "History"));
        settingsMenuButton.setText(LanguageService.text("Instellingen", "Settings"));
    }

    @FXML
    private void goToHome(ActionEvent event) {
        MainLayoutController.loadPage("home.fxml");
    }

    @FXML
    private void goToPlanTrip(ActionEvent event) {
        MainLayoutController.loadPage("home.fxml");
    }

    @FXML
    private void goToFavorites(ActionEvent event) {
        MainLayoutController.loadPage("favorites.fxml");
    }

    @FXML
    private void goToHistory(ActionEvent event) {
        MainLayoutController.loadPage("history.fxml");
    }
    @FXML
    private void goToSettings(ActionEvent event) {
        MainLayoutController.loadPage("settings.fxml");
    }
}
