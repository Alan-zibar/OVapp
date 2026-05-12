package controller;

import Service.NavigationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Vul alle velden in!");
            return;
        }

        if ("admin".equals(username) && "1234".equals(password)) {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Login succesvol! Naar planner...");
            NavigationService.switchScene(event, "home.fxml");
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Ongeldige gebruikersnaam of wachtwoord");
        }
    }

    @FXML
    private void goToRegister(ActionEvent event) {
        NavigationService.switchScene(event, "register.fxml");
    }
}