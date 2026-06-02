package controller;

import Service.LanguageService;
import Service.SettingsService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SettingsController implements LanguageRefreshable {

    @FXML private VBox settingsRoot;

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

    @FXML
    public void initialize() {
        setupOptions();
        loadSavedSettings();
        setupListeners();
        refreshLanguage();

        Platform.runLater(() -> {
            MainLayoutController.applySettings();
            applyLocalSettingsStyle();
        });
    }

    private void setupOptions() {
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
    }

    private void loadSavedSettings() {
        double savedSize = SettingsService.getTextSize();

        textSizeSlider.setValue(savedSize);
        textSizeValueLabel.setText((int) savedSize + "%");
        previewText.setStyle("-fx-font-size: " + (14 * (savedSize / 100.0)) + "px;");

        contrastCombo.setValue(SettingsService.getContrast());
        colorBlindCombo.setValue(SettingsService.getColorFilter());
    }

    private void setupListeners() {
        textSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double value = newVal.doubleValue();

            SettingsService.setTextSize(value);
           