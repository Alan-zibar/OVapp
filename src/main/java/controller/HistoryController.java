package controller;

import Service.LanguageService;
import Service.HistoryService;
import Service.SessionService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HistoryController implements LanguageRefreshable {

    @FXML
    private Label historyTitleLabel;

    @FXML
    private VBox historyContainer;

    @FXML
    private Button clearButton;

    public void initialize() {
        if (historyContainer == null) return;

        historyContainer.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                refreshLanguage();
            }
        });

        refreshLanguage();
    }

    @FXML
    private void clearAllHistory() {
        HistoryService.getInstance().clearHistory();
        refreshLanguage();
    }

    @Override
    public void refreshLanguage() {
        if (historyContainer == null) return;

        historyContainer.getChildren().clear();

        String titleText = LanguageService.text(
                "Reishistorie",
                "Travel history"
        );

        String clearBtnText = LanguageService.text(
                "Verwijder geschiedenis",
                "Clear history"
        );

        String warningText = LanguageService.text(
                "Log in om uw reishistorie te bekijken.",
                "Log in to view your travel history."
        );

        String emptyText = LanguageService.text(
                "Geen reishistorie gevonden.",
                "No travel history found."
        );

        if (historyTitleLabel != null) {
            historyTitleLabel.setText(titleText);
        }

        if (clearButton != null) {
            clearButton.setText(clearBtnText);
        }

        if (!SessionService.isLoggedIn()) {
            if (clearButton != null) clearButton.setDisable(true);

            Label loginWarning = new Label(warningText);
            loginWarning.setStyle("-fx-font-size: 16px; -fx-text-fill: #1a1a1a; -fx-font-weight: bold; -fx-padding: 40 0 0 0;");

            historyContainer.setAlignment(Pos.CENTER);
            historyContainer.getChildren().add(loginWarning);
            return;
        }

        if (HistoryService.getInstance().getHistoryList().isEmpty()) {
            if (clearButton != null) clearButton.setDisable(true);

            Label emptyLabel = new Label(emptyText);
            emptyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #888888; -fx-padding: 20 0 0 0;");

            historyContainer.setAlignment(Pos.CENTER);
            historyContainer.getChildren().add(emptyLabel);
            return;
        }

        if (historyTitleLabel != null) {
            historyTitleLabel.setText(LanguageService.text("Reishistorie", "Travel history"));
        }

        if (clearButton != null) clearButton.setDisable(false);

        historyContainer.setAlignment(Pos.TOP_CENTER);

        java.time.LocalDate today = java.time.LocalDate.now();
        String currentDateFormat = String.format(
                "%02d-%02d-%d",
                today.getDayOfMonth(),
                today.getMonthValue(),
                today.getYear()
        );

        for (String record : HistoryService.getInstance().getHistoryList()) {
            if (record == null || record.trim().isEmpty()) continue;

            String from = LanguageService.text("Onbekend", "Unknown");
            String to = LanguageService.text("Onbekend", "Unknown");
            String typeText = LanguageService.text("Trein | Intercity", "Train | Intercity");
            String dateText = currentDateFormat;

            if (record.contains("|")) {
                String[] parts = record.split("\\|");

                if (parts.length >= 5) {
                    from = parts[0].trim();
                    to = parts[1].trim();
                    typeText = parts[2].trim() + " | " + parts[3].trim();
                    dateText = parts[4].trim();
                } else {
                    if (parts.length > 0) from = parts[0].trim();
                    if (parts.length > 1) to = parts[1].trim();
                    if (parts.length > 2) typeText = parts[2].trim();
                    if (parts.length > 3) dateText = parts[3].trim();
                }
            }

            typeText = typeText
                    .replace("Trein", LanguageService.text("Trein", "Train"))
                    .replace("Lijn", LanguageService.text("Lijn", "Line"));

            javafx.scene.shape.SVGPath pinLabel = new javafx.scene.shape.SVGPath();
            pinLabel.setContent("M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z");
            pinLabel.setFill(javafx.scene.paint.Color.TRANSPARENT);
            pinLabel.setStroke(javafx.scene.paint.Color.web("#111111"));
            pinLabel.setStrokeWidth(2);

            Label routeLabel = new Label(from + "  ➔  " + to);
            routeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #111111;");

            Label infoLabel = new Label(typeText + "   •   " + dateText);
            infoLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #666666;");

            VBox textContainer = new VBox(5, routeLabel, infoLabel);
            HBox.setHgrow(textContainer, javafx.scene.layout.Priority.ALWAYS);

            HBox card = new HBox(15, pinLabel, textContainer);
            card.setAlignment(Pos.CENTER_LEFT);
            card.setPadding(new Insets(15, 20, 15, 20));

            card.setStyle(
                    "-fx-background-color: #ffffff;" +
                            "-fx-border-color: #e0e0e0;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 6;" +
                            "-fx-background-radius: 6;" +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.04), 4, 0, 0, 1);"
            );

            historyContainer.getChildren().add(card);
        }
    }
}