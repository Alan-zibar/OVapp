package controller;

import Service.NavigationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.List;

class User {
    String fullName;
    String username;
    String password;
    User(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }
}

public class RegisterController {

    @FXML private TextField fullNameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;

    private static final List<User> users = new ArrayList<>();

    @FXML
    private void handleRegister(ActionEvent event) {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Alle velden zijn verplicht!");
            return;
        }
        if (!password.equals(confirm)) {
            messageLabel.setText("Wachtwoorden komen niet overeen");
            return;
        }
        for (User u : users) {
            if (u.username.equals(username)) {
                messageLabel.setText("Deze gebruikersnaam bestaat al");
                return;
            }
        }
        users.add(new User(fullName, username, password));
        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText("Account succesvol aangemaakt! Je kunt nu inloggen.");

        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            javafx.application.Platform.runLater(() -> goToLogin(event));
        }).start();
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        NavigationService.switchScene(event, "login.fxml");
    }
}