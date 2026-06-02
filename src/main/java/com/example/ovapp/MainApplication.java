package com.example.ovapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.SettingsController;  // <-- اضافه شد
import javafx.scene.image.Image;

public class MainApplication extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ovapp/view/home.fxml"));
        Scene scene = new Scene(loader.load(), 1100, 700);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApplication.class.getResource("/com/example/ovapp/view/main-layout.fxml")
        );

        stage.getIcons().add(new Image(
                MainApplication.class.getResourceAsStream("/com/example/ovapp/images/ToWhere.png")
        ));


        Scene scene = new Scene(fxmlLoader.load());
 main
        stage.setTitle("ToWhere");
        stage.setScene(scene);
        stage.show();

        SettingsController.applyInitialSettings(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}