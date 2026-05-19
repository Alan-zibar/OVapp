package controller;

import Service.LanguageService;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import java.util.prefs.Preferences;

public class SettingsController implements LanguageRefreshable {

    @FXML private Label titleLabel;
    @FXML private Label textSizeLabel;
    @FXML private Slider textSizeSlider;
    @FXML private Label textSizeValueLabel;
    @FXML private Text previewText;
    @FXML private Label contrastLabel;
    @FXML private ComboBox<String> contrastCombo;
    @FXML private Label colorBlindLabel;
    @FXML private ComboBox<String> colorBlindCombo;
    @FXML private Button resetButton;
    @FXML private Button backButton;

    private Preferences prefs;

    private static final String DEFAULT_CONTRAST = "Standaard";
    private static final String DEFAULT_COLOR_FILTER = "Geen filter";
    private static final double DEFAULT_TEXT_SIZE = 100;

    @FXML
    public void initialize() {
        // Opties voor contrast (deze blijven altijd Nederlandstalig, zoals in wireframe)
        contrastCombo.getItems().setAll(
                "Standaard",
                "Hoog contrast (zwart/wit)",
                "Hoog contrast (geel/zwart)"
        );
        colorBlindCombo.getItems().setAll(
                "Geen filter",
                "Protanopie (roodblind)",
                "Deuteranopie (groenblind)",
                "Tritanopie (blauwblind)"
        );

        prefs = Preferences.userNodeForPackage(getClass());

        // Laad opgeslagen waarden
        double savedSize = prefs.getDouble("textSize", DEFAULT_TEXT_SIZE);
        String savedContrast = prefs.get("contrast", DEFAULT_CONTRAST);
        String savedFilter = prefs.get("colorFilter", DEFAULT_COLOR_FILTER);

        textSizeSlider.setValue(savedSize);
        contrastCombo.setValue(savedContrast);
        colorBlindCombo.setValue(savedFilter);

        // Pas direct toe
        applyTextSize(savedSize);
        applyContrast(savedContrast);
        applyColorFilter(savedFilter);

        // Listeners voor veranderingen
        textSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double val = newVal.doubleValue();
            textSizeValueLabel.setText((int) val + "%");
            applyTextSize(val);
            prefs.putDouble("textSize", val);
        });
        contrastCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                applyContrast(newVal);
                prefs.put("contrast", newVal);
            }
        });
        colorBlindCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                applyColorFilter(newVal);
                prefs.put("colorFilter", newVal);
            }
        });

        refreshLanguage();
    }

    private void applyTextSize(double percent) {
        double fontSize = 14 * (percent / 100.0);
        previewText.setStyle("-fx-font-size: " + fontSize + "px;");
    }

    private void applyContrast(String contrast) {
        Scene scene = getScene();
        if (scene == null) return;
        switch (contrast) {
            case "Hoog contrast (zwart/wit)":
                scene.getRoot().setStyle("-fx-base: black; -fx-background: black; -fx-text-fill: white;");
                break;
            case "Hoog contrast (geel/zwart)":
                scene.getRoot().setStyle("-fx-base: black; -fx-background: black; -fx-text-fill: yellow;");
                break;
            default:
                scene.getRoot().setStyle("");
                break;
        }
    }

    private void applyColorFilter(String filter) {
        Scene scene = getScene();
        if (scene == null) return;
        switch (filter) {
            case "Protanopie (roodblind)":
                scene.getRoot().setStyle("-fx-text-fill: #888888;");
                break;
            case "Deuteranopie (groenblind)":
                scene.getRoot().setStyle("-fx-text-fill: #999999;");
                break;
            case "Tritanopie (blauwblind)":
                scene.getRoot().setStyle("-fx-text-fill: #aaaaaa;");
                break;
            default:
                scene.getRoot().setStyle("-fx-text-fill: black;");
                break;
        }
    }

    private Scene getScene() {
        try {
            return titleLabel.getScene();
        } catch (Exception e) {
            return null;
        }
    }

    @FXML
    private void resetSettings() {
        textSizeSlider.setValue(DEFAULT_TEXT_SIZE);
        contrastCombo.setValue(DEFAULT_CONTRAST);
        colorBlindCombo.setValue(DEFAULT_COLOR_FILTER);

        applyTextSize(DEFAULT_TEXT_SIZE);
        applyContrast(DEFAULT_CONTRAST);
        applyColorFilter(DEFAULT_COLOR_FILTER);

        prefs.putDouble("textSize", DEFAULT_TEXT_SIZE);
        prefs.put("contrast", DEFAULT_CONTRAST);
        prefs.put("colorFilter", DEFAULT_COLOR_FILTER);

        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                LanguageService.text("Instellingen zijn teruggezet naar standaard.",
                        "Settings have been reset to default."),
                ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    private void goBack() {
        MainLayoutController.loadPage("home.fxml");
    }

    @Override
    public void refreshLanguage() {
        titleLabel.setText(LanguageService.text("Instellingen", "Settings"));
        textSizeLabel.setText(LanguageService.text("Tekstgrootte", "Text size"));
        contrastLabel.setText(LanguageService.text("Contrast", "Contrast"));
        colorBlindLabel.setText(LanguageService.text("Kleurenblindheid filter", "Color blindness filter"));
        resetButton.setText(LanguageService.text("Standaard instellingen herstellen", "Reset to default settings"));
        backButton.setText(LanguageService.text("Terug", "Back"));
    }

    public static void applyInitialSettings(Scene scene) {
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        String savedContrast = prefs.get("contrast", DEFAULT_CONTRAST);
        String savedFilter = prefs.get("colorFilter", DEFAULT_COLOR_FILTER);
        switch (savedContrast) {
            case "Hoog contrast (zwart/wit)":
                scene.getRoot().setStyle("-fx-base: black; -fx-background: black; -fx-text-fill: white;");
                break;
            case "Hoog contrast (geel/zwart)":
                scene.getRoot().setStyle("-fx-base: black; -fx-background: black; -fx-text-fill: yellow;");
                break;
            default:
                scene.getRoot().setStyle("");
                break;
        }
        if (!savedFilter.equals(DEFAULT_COLOR_FILTER)) {
            scene.getRoot().setStyle("-fx-text-fill: #aaaaaa;");
        }
    }
}