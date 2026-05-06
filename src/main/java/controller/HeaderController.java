package controller;

import Service.LanguageService;
import Service.SessionService;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HeaderController {

    private static HeaderController instance;

    @FXML
    private Label currentTimeTextLabel;

    @FXML
    private Label currentTimeLabel;

    @FXML
    private MenuButton languageMenuButton;

    @FXML
    private Button loginTopButton;

    @FXML
    private Button logoutTopButton;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @FXML
    private void initialize() {
        instance = this;
        startClock();
        updateTexts();
        updateLoginStatus();
    }

    public static void refreshHeader() {
        if (instance != null) {
            instance.updateTexts();
            instance.updateLoginStatus();
        }
    }

    private void startClock() {
        updateCurrentTime();

        Timeline clockTimeline = new Timeline(
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
        LanguageService.useDutch();
        updateTexts();
        updateLoginStatus();
        SidebarController.refreshSidebar();
        MainLayoutController.refreshCurrentPage();
    }

    @FXML
    private void setEnglishLanguage() {
        LanguageService.useEnglish();
        updateTexts();
        updateLoginStatus();
        SidebarController.refreshSidebar();
        MainLayoutController.refreshCurrentPage();
    }

    private void updateTexts() {
        languageMenuButton.setText(LanguageService.getLanguageCode());
        currentTimeTextLabel.setText(LanguageService.text("Huidige tijd:", "Current time:"));
        logoutTopButton.setText(LanguageService.text("Uitloggen", "Logout"));

        if (!SessionService.isLoggedIn()) {
            loginTopButton.setText(LanguageService.text("Inloggen", "Login"));
        }
    }

    private void updateLoginStatus() {
        if (SessionService.isLoggedIn()) {
            loginTopButton.setText(SessionService.getUsername());
            logoutTopButton.setVisible(true);
            logoutTopButton.setManaged(true);
        } else {
            loginTopButton.setText(LanguageService.text("Inloggen", "Login"));
            logoutTopButton.setVisible(false);
            logoutTopButton.setManaged(false);
        }
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        if (!SessionService.isLoggedIn()) {
            MainLayoutController.loadPage("login.fxml");
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        SessionService.logout();
        refreshHeader();
        MainLayoutController.loadPage("home.fxml");
    }
}
