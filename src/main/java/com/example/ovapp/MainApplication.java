package com.example.ovapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.SettingsController;  // <-- اضافه شد

public class MainApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApplication.class.getResource("/com/example/ovapp/view/main-layout.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("WhereTo");
        stage.setScene(scene);
        stage.show();

        SettingsController.applyInitialSettings(scene);
    }
}