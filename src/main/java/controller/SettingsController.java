package controller;

import Service.NavigationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.prefs.Preferences;

public class SettingsController {

    @FXML private Slider textSizeSlider;
    @FXML private Label textSizeValue;
    @FXML private Text previewText;
    @FXML private ComboBox<String> contrastCombo;
    @FXML private ComboBox<String> colorBlindCombo;

    private Preferences prefs;
    private Scene mainScene; // reference naar de scene om contrast aan te passen

    private final String DEFAULT_CONTRAST = "Standaard";
    private final String DEFAULT_COLOR_FILTER = "Geen filter";
    private final double DEFAULT_TEXT_SIZE = 100;

    @FXML
    public void initialize() {
        prefs = Preferences.userNodeForPackage(getClass());

        // Laad opgeslagen waarden
        double savedSize = prefs.getDouble("textSize", DEFAULT_TEXT_SIZE);
        String savedContrast = prefs.get("contrast", DEFAULT_CONTRAST);
        String savedFilter = prefs.get("colorFilter", DEFAULT_COLOR_FILTER);

        textSizeSlider.setValue(savedSize);
        contrastCombo.setValue(savedContrast);
        colorBlindCombo.setValue(savedFilter);

        // Pas direct de opgeslagen instellingen toe
        applyTextSize(savedSize);
        applyContrast(savedContrast);
        applyColorFilter(savedFilter);

        // Listener voor tekstgrootte
        textSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double value = newVal.doubleValue();
            textSizeValue.setText((int) value + "%");
            applyTextSize(value);
            prefs.putDouble("textSize", value);
        });

        // Listener voor contrast
        contrastCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                applyContrast(newVal);
                prefs.put("contrast", newVal);
            }
        });

        // Listener voor kleurenfilter
        colorBlindCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                applyColorFilter(newVal);
                prefs.put("colorFilter", newVal);
            }
        });
    }

    private void applyTextSize(double percent) {
        double fontSize = 14 * (percent / 100.0);
        previewText.setStyle("-fx-font-size: " + fontSize + "px;");
        // Later eventueel de hele scene aanpassen
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
        // Voor een echte kleurenblindheidsfilter zou je een CSS class of een effect moeten gebruiken.
        // Omdat JavaFX dit niet native ondersteunt, is dit een placeholder.
        // Je kunt hier later een library zoals 'ColorblindFX' gebruiken, maar voor nu is het voldoende.
        switch (filter) {
            case "Protanopie (roodblind)":
                // placeholder
                break;
            case "Deuteranopie (groenblind)":
                break;
            case "Tritanopie (blauwblind)":
                break;
            default:
                break;
        }
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
        contrastCombo.setValue(DEFAULT_CONTRAST);
        colorBlindCombo.setValue(DEFAULT_COLOR_FILTER);

        applyTextSize(DEFAULT_TEXT_SIZE);
        applyContrast(DEFAULT_CONTRAST);
        applyColorFilter(DEFAULT_COLOR_FILTER);

        prefs.putDouble("textSize", DEFAULT_TEXT_SIZE);
        prefs.put("contrast", DEFAULT_CONTRAST);
        prefs.put("colorFilter", DEFAULT_COLOR_FILTER);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Instellingen zijn teruggezet naar standaard.", ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    private void goBack(ActionEvent event) {
        NavigationService.switchScene(event, "home.fxml");
    }
}