package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
        try {
            FXMLLoader loader = new FXMLLoader(MainLayoutController.class.getResource(
                    "/com/example/ovapp/view/" + fxmlFile
            ));

            Node page = loader.load();
            Object controller = loader.getController();

            instance.currentCenterController = controller;
            instance.mainBorderPane.setCenter(page);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void refreshCurrentPage() {
        if (instance == null || instance.currentCenterController == null) {
            return;
        }

        if (instance.currentCenterController instanceof LanguageRefreshable refreshableController) {
            refreshableController.refreshLanguage();
        }
    }
}
