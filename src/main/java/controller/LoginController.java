package controller;

import Service.UserService;
import Service.LanguageService;
import Service.SessionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements LanguageRefreshable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField visiblePasswordField;

    @FXML
    private Button togglePasswordButton;

    @FXML
    private Label messageLabel;

    @FXML
    private Label loginTitleLabel;

    @FXML
    private Label loginDescriptionLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Button loginFormButton;

    @FXML
    private Label orLabel;

    @FXML
    private Button createAccountButton;

    @FXML
    private Button backHomeButton;

    @FXML
    private void initialize() {
        refreshLanguage();
    }

    @FXML
    private void goToRegister(ActionEvent event) {
        MainLayoutController.loadPage("register.fxml");
    }

    @Override
    public void refreshLanguage() {
        loginTitleLabel.setText(LanguageService.text("Inloggen", "Login"));

        loginDescriptionLabel.setText(LanguageService.text(
                "Log in om je reizen, favorieten en instellingen te bewaren\nen meer mogelijkheden te gebruiken.",
                "Log in to save your trips, favorites and settings\nand use more features."
        ));

        usernameLabel.setText(LanguageService.text(
                "E-mailadres of gebruikersnaam",
                "Email address or username"
        ));

        usernameField.setPromptText(LanguageService.text(
                "Voer je e-mailadres of gebruikersnaam in",
                "Enter your email address or username"
        ));

        passwordLabel.setText(LanguageService.text("Wachtwoord", "Password"));

        passwordField.setPromptText(LanguageService.text(
                "Voer je wachtwoord in",
                "Enter your password"
        ));

        loginFormButton.setText(LanguageService.text("🔒  Inloggen", "🔒  Login"));
        orLabel.setText(LanguageService.text("of", "or"));

        createAccountButton.setText(LanguageService.text(
                "👥  Nieuw account aanmaken",
                "👥  Create new account"
        ));

        backHomeButton.setText(LanguageService.text(
                "←  Terug naar home",
                "←  Back to home"
        ));

        if (messageLabel.getText() != null && !messageLabel.getText().isEmpty()) {
            messageLabel.setText(LanguageService.text(
                    "Gebruikersnaam of wachtwoord is onjuist.",
                    "Username or password is incorrect."
            ));
        }
    }


    private boolean passwordVisible = false;

    @FXML
    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;

        if (passwordVisible) {
            visiblePasswordField.setText(passwordField.getText());
            visiblePasswordField.setVisible(true);
            passwordField.setVisible(false);
        } else {
            passwordField.setText(visiblePasswordField.getText());
            passwordField.setVisible(true);
            visiblePasswordField.setVisible(false);
        }
    }


    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            messageLabel.setText(LanguageService.text(
                    "Vul je gebruikersnaam en wachtwoord in.",
                    "Enter your username and password."
            ));
            return;
        }

        if (UserService.login(username, password)) {
            SessionService.login(username);
            HeaderController.refreshHeader();
            MainLayoutController.loadPage("home.fxml");
        } else {
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            messageLabel.setText(LanguageService.text(
                    "Gebruikersnaam of wachtwoord is onjuist.",
                    "Username or password is incorrect."
            ));
        }
    }

    @FXML
    private void goBackToHome(ActionEvent event) {
        MainLayoutController.loadPage("home.fxml");
    }
}
