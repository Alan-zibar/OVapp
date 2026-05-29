package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainLayoutController {

    @FXML
    private BorderPane mainBorderPane;

    private static MainLayoutController instance;
    private Object currentCenterController;

    @FXML
    private void initialize() {
        instance = this;
        loadPage("home.fxml");
    }

    public static void loadPage(String fxmlFile) {
        if (instance == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainLayoutController.class.getResource("/com/example/ovapp/view/" + fxmlFile)
            );
            Node page = loader.load();
            instance.currentCenterController = loader.getController();
            instance.mainBorderPane.setCenter(page);

            Scene scene = instance.mainBorderPane.getScene();
            if (scene != null) {
                SettingsController.applyInitialSettings(scene);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void refreshCurrentPage() {
        if (instance == null || instance.currentCenterController == null) return;
        if (instance.currentCenterController instanceof LanguageRefreshable) {
            ((LanguageRefreshable) instance.currentCenterController).refreshLanguage();
        }
    }
}