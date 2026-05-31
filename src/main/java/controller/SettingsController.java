package controller;

import Service.LanguageService;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.Node;
import javafx.scene.Parent;
import java.util.prefs.Preferences;

public class SettingsController implements LanguageRefreshable {

    @FXML private Label titleLabel;
    @FXML private Label textSizeLabel;
    @FXML private Slider textSizeSlider;
    @FXML private Text previewText;
    @FXML private Label contrastLabel;
    @FXML private Label chooseContrastLabel;
    @FXML private Label colorBlindLabel;
    @FXML private Label chooseColorBlindLabel;
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
        scene.getRoot().setStyle("-fx-font-size: " + baseFont + "px;");
        applyFontSizeToAllNodes(scene.getRoot(), baseFont);

        StringBuilder contrastStyle = new StringBuilder();
        switch (contrast) {
            case "Hoog contrast (zwart/wit)":
                contrastStyle.append("-fx-base: black; -fx-background: black; -fx-text-fill: white; -fx-control-inner-background: black;");
                break;
            case "Hoog contrast (geel/zwart)":
                contrastStyle.append("-fx-base: black; -fx-background: black; -fx-text-fill: yellow; -fx-control-inner-background: black;");
                break;
            default:
                contrastStyle.append("-fx-base: white; -fx-background: white; -fx-text-fill: black;");
                break;
        }
        scene.getRoot().setStyle(scene.getRoot().getStyle() + ";" + contrastStyle.toString());
        applyContrastToAllNodes(scene.getRoot(), contrast);

        applyColorFilterEffect(scene, colorFilter);
    }

    private void applyFontSizeToAllNodes(Node node, double fontSize) {
        String style = "-fx-font-size: " + fontSize + "px;";
        if (node instanceof Label) ((Label) node).setStyle(style);
        else if (node instanceof Button) ((Button) node).setStyle(style);
        else if (node instanceof Text) ((Text) node).setStyle(style);
        else if (node instanceof TextField) ((TextField) node).setStyle(style);
        else if (node instanceof PasswordField) ((PasswordField) node).setStyle(style);
        else if (node instanceof ComboBox) ((ComboBox<?>) node).setStyle(style);
        else if (node instanceof RadioButton) ((RadioButton) node).setStyle(style);
        else if (node instanceof ToggleButton) ((ToggleButton) node).setStyle(style);
        else if (node instanceof MenuButton) ((MenuButton) node).setStyle(style);
        else if (node instanceof DatePicker) ((DatePicker) node).setStyle(style);
        else if (node instanceof Spinner) ((Spinner<?>) node).setStyle(style);
        if (node instanceof Parent) {
            for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
                applyFontSizeToAllNodes(child, fontSize);
            }
        }
    }

    private void applyContrastToAllNodes(Node node, String contrast) {
        String textFill = "";
        String background = "";
        if ("Hoog contrast (zwart/wit)".equals(contrast)) {
            textFill = "white";
            background = "black";
        } else if ("Hoog contrast (geel/zwart)".equals(contrast)) {
            textFill = "yellow";
            background = "black";
        } else {
            textFill = "black";
            background = "white";
        }
        String style = "-fx-text-fill: " + textFill + "; -fx-background-color: " + background + ";";
        if (node instanceof Label) ((Label) node).setStyle(((Label) node).getStyle() + ";" + style);
        else if (node instanceof Button) ((Button) node).setStyle(((Button) node).getStyle() + ";" + style);
        else if (node instanceof TextField) ((TextField) node).setStyle(((TextField) node).getStyle() + ";" + style);
        else if (node instanceof PasswordField) ((PasswordField) node).setStyle(((PasswordField) node).getStyle() + ";" + style);
        else if (node instanceof ComboBox) ((ComboBox<?>) node).setStyle(((ComboBox<?>) node).getStyle() + ";" + style);
        else if (node instanceof RadioButton) ((RadioButton) node).setStyle(((RadioButton) node).getStyle() + ";" + style);
        else if (node instanceof ToggleButton) ((ToggleButton) node).setStyle(((ToggleButton) node).getStyle() + ";" + style);
        else if (node instanceof MenuButton) ((MenuButton) node).setStyle(((MenuButton) node).getStyle() + ";" + style);
        else if (node instanceof DatePicker) ((DatePicker) node).setStyle(((DatePicker) node).getStyle() + ";" + style);
        else if (node instanceof Spinner) ((Spinner<?>) node).setStyle(((Spinner<?>) node).getStyle() + ";" + style);
        if (node instanceof Parent) {
            for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
                applyContrastToAllNodes(child, contrast);
            }
        }
    }

    private void applyColorFilterEffect(Scene scene, String filter) {
        if (scene == null) return;
        scene.getRoot().setEffect(null);
        if ("Geen filter".equals(filter)) return;
        ColorAdjust colorAdjust = new ColorAdjust();
        switch (filter) {
            case "Protanopie (roodblind)":
                colorAdjust.setHue(-0.6);
                colorAdjust.setSaturation(-0.8);
                break;
            case "Deuteranopie (groenblind)":
                colorAdjust.setHue(0.5);
                colorAdjust.setSaturation(-0.8);
                break;
            case "Tritanopie (blauwblind)":
                colorAdjust.setHue(0.7);
                colorAdjust.setSaturation(-0.9);
                break;
            default:
                return;
        }
        scene.getRoot().setEffect(colorAdjust);
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
        chooseContrastLabel.setText(LanguageService.text(
                "Kies een weergave die het beste bij jou past:",
                "Choose a display that suits you best:"));
        colorBlindLabel.setText(LanguageService.text("Kleurenblindheid filters", "Color blindness filters"));
        chooseColorBlindLabel.setText(LanguageService.text(
                "Kies het type kleurenblindheid:",
                "Choose the type of color blindness:"));
        infoLabel.setText(LanguageService.text("De wijzigingen worden direct toegepast op de app.",
                "Changes are applied to the app immediately."));
        resetButton.setText(LanguageService.text("Standaard instellingen herstellen", "Reset to default settings"));

        contrastStandard.setText(LanguageService.text("Standaard contrast", "Standard contrast"));
        contrastBlackWhite.setText(LanguageService.text("Hoog contrast (zwart / wit)", "High contrast (black / white)"));
        contrastYellowBlack.setText(LanguageService.text("Hoog contrast (geel / zwart)", "High contrast (yellow / black)"));

        filterNone.setText(LanguageService.text("Geen filter", "No filter"));
        filterProtan.setText(LanguageService.text("Protanopie (roodblind)", "Protanopia (red-blind)"));
        filterDeutan.setText(LanguageService.text("Deuteranopie (groenblind)", "Deuteranopia (green-blind)"));
        filterTritan.setText(LanguageService.text("Tritanopie (blauwblind)", "Tritanopia (blue-blind)"));
    }

    public static void applyInitialSettings(Scene scene) {
        if (scene == null) return;
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        double textSize = prefs.getDouble("textSize", DEFAULT_TEXT_SIZE);
        String contrast = prefs.get("contrast", DEFAULT_CONTRAST);
        String colorFilter = prefs.get("colorFilter", DEFAULT_COLOR_FILTER);
        double baseFont = 14 * (textSize / 100.0);
        scene.getRoot().setStyle("-fx-font-size: " + baseFont + "px;");
        SettingsController temp = new SettingsController();
        temp.applyFontSizeToAllNodes(scene.getRoot(), baseFont);
        StringBuilder contrastStyle = new StringBuilder();
        switch (contrast) {
            case "Hoog contrast (zwart/wit)":
                contrastStyle.append("-fx-base: black; -fx-background: black; -fx-text-fill: white; -fx-control-inner-background: black;");
                break;
            case "Hoog contrast (geel/zwart)":
                contrastStyle.append("-fx-base: black; -fx-background: black; -fx-text-fill: yellow; -fx-control-inner-background: black;");
                break;
            default:
                contrastStyle.append("-fx-base: white; -fx-background: white; -fx-text-fill: black;");
                break;
        }
        scene.getRoot().setStyle(scene.getRoot().getStyle() + ";" + contrastStyle.toString());
        temp.applyContrastToAllNodes(scene.getRoot(), contrast);
        ColorAdjust colorAdjust = new ColorAdjust();
        switch (colorFilter) {
            case "Protanopie (roodblind)":
                colorAdjust.setHue(-0.6);
                colorAdjust.setSaturation(-0.8);
                break;
            case "Deuteranopie (groenblind)":
                colorAdjust.setHue(0.5);
                colorAdjust.setSaturation(-0.8);
                break;
            case "Tritanopie (blauwblind)":
                colorAdjust.setHue(0.7);
                colorAdjust.setSaturation(-0.9);
                break;
            default:
                scene.getRoot().setEffect(null);
                return;
        }
        scene.getRoot().setEffect(colorAdjust);
    }
}