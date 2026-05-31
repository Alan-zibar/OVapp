package controller;

import Service.LanguageService;
import Service.SessionService;
import Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegisterController implements LanguageRefreshable {

    @FXML private Label registerTitleLabel;
    @FXML private Label fullNameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label passwordLabel;
    @FXML private Label confirmPasswordLabel;
    @FXML private Label messageLabel;
    @FXML private TextField fullNameField;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button createAccountButton;
    @FXML private Button backToLoginButton;
    @FXML private Button backHomeButton;

    @FXML
    private void initialize() {
        refreshLanguage();
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String fullName = fullNameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (fullName.isBlank() || username.isBlank() || email.isBlank()
                || password.isBlank() || confirmPassword.isBlank()) {
            messageLabel.setText(LanguageService.text("Vul alle velden in.", "Fill in all fields."));
            return;
        }
        if (!email.contains("@")) {
            messageLabel.setText(LanguageService.text("Voer een geldig e-mailadres in.", "Enter a valid email address."));
            return;
        }
        if (!password.equals(confirmPassword)) {
            messageLabel.setText(LanguageService.text("De wachtwoorden komen niet overeen.", "The passwords do not match."));
            return;
        }
        if (UserService.usernameExists(username)) {
            messageLabel.setText(LanguageService.text("Deze gebruikersnaam bestaat al.", "This username already exists."));
            return;
        }
        UserService.registerUser(fullName, username, email, password);
        SessionService.login(username);
        HeaderController.refreshHeader();
        MainLayoutController.loadPage("home.fxml");
    }

    @FXML
    private void goBackToLogin(ActionEvent event) {
        MainLayoutController.loadPage("login.fxml");
    }

    @FXML
    private void goBackToHome(ActionEvent event) {
        MainLayoutController.loadPage("home.fxml");
    }

    @Override
    public void refreshLanguage() {
        registerTitleLabel.setText(LanguageService.text("Account aanmaken", "Create account"));
        fullNameLabel.setText(LanguageService.text("Volledige naam", "Full name"));
        usernameLabel.setText(LanguageService.text("Gebruikersnaam", "Username"));
        emailLabel.setText(LanguageService.text("E-mailadres", "Email address"));
        passwordLabel.setText(LanguageService.text("Wachtwoord", "Password"));
        confirmPasswordLabel.setText(LanguageService.text("Wachtwoord herhalen", "Repeat password"));
        fullNameField.setPromptText(LanguageService.text("Voer je volledige naam in", "Enter your full name"));
        usernameField.setPromptText(LanguageService.text("Kies een gebruikersnaam", "Choose a username"));
        emailField.setPromptText(LanguageService.text("Voer je e-mailadres in", "Enter your email address"));
        passwordField.setPromptText(LanguageService.text("Voer je wachtwoord in", "Enter your password"));
        confirmPasswordField.setPromptText(LanguageService.text("Herhaal je wachtwoord", "Repeat your password"));
        createAccountButton.setText(LanguageService.text("👥  Account aanmaken", "👥  Create account"));
        backToLoginButton.setText(LanguageService.text("←  Terug naar inloggen", "←  Back to login"));
        backHomeButton.setText(LanguageService.text("←  Terug naar home", "←  Back to home"));
    }
}