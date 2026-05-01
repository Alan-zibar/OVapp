package Service;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class NavigationService {

    private static final String VIEW_PATH = "/com/example/ovapp/view/";

    public static void switchScene(ActionEvent event, String fxmlFile) {
        try {
            URL viewUrl = NavigationService.class.getResource(VIEW_PATH + fxmlFile);

            if (viewUrl == null) {
                throw new IOException("FXML bestand niet gevonden: " + fxmlFile);
            }

            Parent root = FXMLLoader.load(viewUrl);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
