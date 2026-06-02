package controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HistoryController implements LanguageRefreshable {

    public static boolean isLoggedIn = false;

    @FXML
    private VBox historyContainer;
    @FXML
    private javafx.scene.control.Button clearButton;

    public void initialize() {
        if (historyContainer == null) return;
        historyContainer.getChildren().clear();

        if (clearButton != null) {
            clearButton.setDisable(true);
        }
        historyContainer.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                refreshLanguage();
            }
        });
        refreshLanguage();
    }

    @FXML
    private void clearAllHistory() {
        Service.HistoryService.getInstance().clearHistory();

        if (historyContainer != null) {
            historyContainer.getChildren().clear();

            try {
                java.util.Locale currentLocale = java.util.Locale.getDefault();
                java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("strings", currentLocale);
                Label emptyLabel = new Label(bundle.getString("history.empty"));
                emptyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #888888; -fx-padding: 20 0 0 0;");
                historyContainer.setAlignment(Pos.CENTER);
                historyContainer.getChildren().add(emptyLabel);
            } catch (Exception e) {
                Label emptyLabel = new Label("Geen reishistorie gevonden.");
                emptyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #888888; -fx-padding: 20 0 0 0;");
                historyContainer.setAlignment(Pos.CENTER);
                historyContainer.getChildren().add(emptyLabel);
            }
        }
        if (clearButton != null) clearButton.setDisable(true);
    }

    // ARTIK ANA MERKEZ BURASI: Her tıklamada veya dil değişiminde canlı olarak tetiklenir!
    @Override
    public void refreshLanguage() {
        if (historyContainer == null) return;

        // Ekranı temizle
        historyContainer.getChildren().clear();

        java.util.Locale currentLocale = java.util.Locale.getDefault();

        // Varsayılan Hollandaca yazılar (Eğer dosya bulunamazsa çökmesin diye hazır tutuyoruz)
        String clearBtnText = "Verwijder geschiedenis";
        String warningText = "Log in om uw reishistorie te bekijken.";
        String emptyText = "Geen reishistorie gevonden.";

        // TRY-CATCH ile dosyayı okumayı deniyoruz, dosya yoksa catch bloğuna düşüp çökmeyi önleyecek!
        try {
            java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("strings", currentLocale);

            if (bundle.containsKey("history.clearButton")) clearBtnText = bundle.getString("history.clearButton");
            if (bundle.containsKey("history.loginWarning")) warningText = bundle.getString("history.loginWarning");
            if (bundle.containsKey("history.empty")) emptyText = bundle.getString("history.empty");
        } catch (java.util.MissingResourceException e) {
            System.out.println("UYARI: Dil dosyaları src/main/resources altında bulunamadı, varsayılan metinler yükleniyor.");
            // Eğer dil dosyaların bir alt klasördeyse, üstteki "strings" yerine "com/example/ovapp/strings" gibi tam yol yazmayı deneyebilirsin.
        }

        // Buton yazısını güncelle
        if (clearButton != null) {
            clearButton.setText(clearBtnText);
        }

        // DURUM 1: Eğer kullanıcı giriş yapmadıysa siyah renkli uyarıyı bas ve dur!
        if (!isLoggedIn) {
            if (clearButton != null) clearButton.setDisable(true);

            Label loginWarning = new Label(warningText);
            loginWarning.setStyle("-fx-font-size: 16px; -fx-text-fill: #1a1a1a; -fx-font-weight: bold; -fx-padding: 40 0 0 0;");

            historyContainer.setAlignment(Pos.CENTER);
            historyContainer.getChildren().add(loginWarning);
            return;
        }

        // DURUM 2: Giriş yapılmış ama geçmiş listesi tamamen boşsa boş uyarısı bas!
        if (Service.HistoryService.getInstance().getHistoryList().isEmpty()) {
            if (clearButton != null) clearButton.setDisable(true);

            Label emptyLabel = new Label(emptyText);
            emptyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #888888; -fx-padding: 20 0 0 0;");
            historyContainer.setAlignment(Pos.CENTER);
            historyContainer.getChildren().add(emptyLabel);
            return;
        }

        // DURUM 3: Giriş yapılmış ve veri var! Temizleme butonunu aç ve kartları döngüyle bas
        if (clearButton != null) clearButton.setDisable(false);

        java.time.LocalDate today = java.time.LocalDate.now();
        String currentDateFormat = String.format("%02d-%02d-%d", today.getDayOfMonth(), today.getMonthValue(), today.getYear());

        for (String record : Service.HistoryService.getInstance().getHistoryList()) {
            if (record == null || record.trim().isEmpty()) continue;

            String from = "Onbekend";
            String to = "Onbekend";
            String typeText = "Trein | Intercity";
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

            if (currentLocale.getLanguage().equals("en")) {
                typeText = typeText.replace("Trein", "Train").replace("Lijn", "Line");
            }

            // Kart Arayüz Elemanlarının Çizimi
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
            javafx.scene.layout.HBox.setHgrow(textContainer, javafx.scene.layout.Priority.ALWAYS);

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