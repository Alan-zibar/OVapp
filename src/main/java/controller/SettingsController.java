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
    @FXML private Text previewText;
    @FXML private Label contrastLabel;
    @FXML private Label colorBlindLabel;
    @FXML private Label infoLabel;
    @FXML private Button resetButton;

    @FXML private RadioButton contrastStandard;
    @FXML private RadioButton contrastBlackWhite;
    @FXML private RadioButton contrastYellowBlack;

    @FXML private RadioButton filterNone;
    @FXML private RadioButton filterProtan;
    @FXML private RadioButton filterDeutan;
    @FXML private RadioButton filterTritan;

    private ToggleGroup contrastGroup;
    private ToggleGroup colorBlindGroup;
    private Preferences prefs;

    private static final String DEFAULT_CONTRAST = "Standaard";
    private static final String DEFAULT_COLOR_FILTER = "Geen filter";
    private static final double DEFAULT_TEXT_SIZE = 100;

    @FXML
    public void initialize() {
        contrastGroup = new ToggleGroup();
        contrastStandard.setToggleGroup(contrastGroup);
        contrastBlackWhite.setToggleGroup(contrastGroup);
        contrastYellowBlack.setToggleGroup(contrastGroup);

        colorBlindGroup = new ToggleGroup();
        filterNone.setToggleGroup(colorBlindGroup);
        filterProtan.setToggleGroup(colorBlindGroup);
        filterDeutan.setToggleGroup(colorBlindGroup);
        filterTritan.setToggleGroup(colorBlindGroup);

        prefs = Preferences.userNodeForPackage(getClass());

        double savedSize = prefs.getDouble("textSize", DEFAULT_TEXT_SIZE);
        String savedContrast = prefs.get("contrast", DEFAULT_CONTRAST);
        String savedFilter = prefs.get("colorFilter", DEFAULT_COLOR_FILTER);

        textSizeSlider.setValue(savedSize);
        updatePreviewTextSize(savedSize);
        setSelectedRadio(contrastGroup, savedContrast);
        setSelectedRadio(colorBlindGroup, savedFilter);

        applyAllSettings();

        textSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double val = newVal.doubleValue();
            updatePreviewTextSize(val);
            prefs.putDouble("textSize", val);
            applyAllSettings();
        });

        contrastGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String contrast = (String) newVal.getUserData();
                prefs.put("contrast", contrast);
                applyAllSettings();
            }
        });

        colorBlindGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String filter = (String) newVal.getUserData();
                prefs.put("colorFilter", filter);
                applyAllSettings();
            }
        });

        refreshLanguage();
    }

    private void setSelectedRadio(ToggleGroup group, String value) {
        for (Toggle toggle : group.getToggles()) {
            if (toggle.getUserData() != null && toggle.getUserData().equals(value)) {
                group.selectToggle(toggle);
                break;
            }
        }
    }

    private void updatePreviewTextSize(double percent) {
        double fontSize = 14 * (percent / 100.0);
        previewText.setStyle("-fx-font-size: " + fontSize + "px;");
    }

    private void applyAllSettings() {
        Scene scene = getScene();
        if (scene == null) return;

        double textSizePercent = prefs.getDouble("textSize", DEFAULT_TEXT_SIZE);
        String contrast = prefs.get("contrast", DEFAULT_CONTRAST);
        String colorFilter = prefs.get("colorFilter", DEFAULT_COLOR_FILTER);

        double baseFont = 14 * (textSizePercent / 100.0);
        StringBuilder style = new StringBuilder();
        style.append("-fx-font-size: ").append(baseFont).append("px;");

        switch (contrast) {
            case "Hoog contrast (zwart/wit)":
                style.append("-fx-base: black; -fx-background: black; -fx-text-fill: white; -fx-control-inner-background: black;");
                break;
            case "Hoog contrast (geel/zwart)":
                style.append("-fx-base: black; -fx-background: black; -fx-text-fill: yellow; -fx-control-inner-background: black;");
                break;
            default:
                style.append("-fx-base: white; -fx-background: white; -fx-text-fill: black;");
                break;
        }

        switch (colorFilter) {
            case "Protanopie (roodblind)":
                style.append("-fx-opacity: 0.92;");
                break;
            case "Deuteranopie (groenblind)":
                style.append("-fx-opacity: 0.95;");
                break;
            case "Tritanopie (blauwblind)":
                style.append("-fx-opacity: 0.90;");
                break;
            default:
                break;
        }

        scene.getRoot().setStyle(style.toString());
    }

    private Scene getScene() {
        try {
            return previewText.getScene();
        } catch (Exception e) {
            return null;
        }
    }

    @FXML
    private void resetSettings() {
        textSizeSlider.setValue(DEFAULT_TEXT_SIZE);
        setSelectedRadio(contrastGroup, DEFAULT_CONTRAST);
        setSelectedRadio(colorBlindGroup, DEFAULT_COLOR_FILTER);

        prefs.putDouble("textSize", DEFAULT_TEXT_SIZE);
        prefs.put("contrast", DEFAULT_CONTRAST);
        prefs.put("colorFilter", DEFAULT_COLOR_FILTER);

        updatePreviewTextSize(DEFAULT_TEXT_SIZE);
        applyAllSettings();

        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                LanguageService.text("Instellingen zijn teruggezet naar standaard.",
                        "Settings have been reset to default."),
                ButtonType.OK);
        alert.showAndWait();
    }

    @Override
    public void refreshLanguage() {
        titleLabel.setText(LanguageService.text("Instellingen", "Settings"));
        textSizeLabel.setText(LanguageService.text("Tekstgrootte", "Text size"));
        contrastLabel.setText(LanguageService.text("Hoog contrast (kleurenweergave)", "High contrast (color display)"));
        colorBlindLabel.setText(LanguageService.text("Kleurenblindheid filters", "Color blindness filters"));
        infoLabel.setText(LanguageService.text("De wijzigingen worden direct toegepast op de app.",
                "Changes are applied to the app immediately."));
        resetButton.setText(LanguageService.text("Standaard instellingen herstellen", "Reset to default settings"));
    }

    public static void applyInitialSettings(Scene scene) {
        if (scene == null) return;
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        double textSize = prefs.getDouble("textSize", DEFAULT_TEXT_SIZE);
        String contrast = prefs.get("contrast", DEFAULT_CONTRAST);
        double baseFont = 14 * (textSize / 100.0);
        StringBuilder style = new StringBuilder();
        style.append("-fx-font-size: ").append(baseFont).append("px;");
        switch (contrast) {
            case "Hoog contrast (zwart/wit)":
                style.append("-fx-base: black; -fx-background: black; -fx-text-fill: white;");
                break;
            case "Hoog contrast (geel/zwart)":
                style.append("-fx-base: black; -fx-background: black; -fx-text-fill: yellow;");
                break;
            default:
                break;
        }
        scene.getRoot().setStyle(style.toString());
    }
}