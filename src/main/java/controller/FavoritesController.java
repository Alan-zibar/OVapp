package controller;


import Service.LanguageService;
import Service.FavoriteService;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;



public class FavoritesController implements LanguageRefreshable {

    @FXML
    private void backToHome(){
        MainLayoutController.loadPage("home.fxml");
    }



    @FXML
    private VBox favoritesContainer;

    @FXML
    private Button backButton;
    
    @FXML
    private void initialize() {

        backButton.setText(LanguageService.text(
            "← Terug naar home",
            "← Back to home"


        ));
       
        for (model.Trip trip : FavoriteService.getFavorites()){
            HBox card = new HBox(25);
            card.setAlignment(Pos.CENTER_LEFT);
            card.setStyle(
            "-fx-border-color: #9a9a9a;" +
            "-fx-border-width: 1.5;" +
            "-fx-background-color: white;" +
            "-fx-padding: 20;"            
            );

            card.setPrefHeight(125);
            card.setPrefWidth(770);
            card.setMaxWidth(770);

            Label iconLabel= new Label("📍");
            iconLabel.setStyle("-fx-font-size: 30px;");

            Label tripLabel = new Label(trip.getFromStation() + " → " + trip.getToStation());
            Label detailsLabel = new Label (
                ("BUS".equals(trip.getTransportMode()) ? "Bus | " : "  Train | ") +
                trip.getTransportType() + " | " +
                trip.getDepartureTime() + " ± " +
                trip.getExpectedArrivalTime()
            );

            
            VBox textBox = new VBox(8);
            textBox.getChildren().addAll(tripLabel, detailsLabel);
            detailsLabel.setStyle("-fx-font-size: 16px;");
            tripLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");


            card.getChildren().add(iconLabel);
            card.getChildren().add(textBox);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            card.getChildren().add(spacer);

            Label favoriteIcon = new Label("🗑");
            favoriteIcon.setStyle("-fx-font-size: 32px;");
            favoriteIcon.setOnMouseClicked(event -> {
                FavoriteService.removeFavorite(trip);
                favoritesContainer.getChildren().remove(card);
            });

            card.getChildren().add(favoriteIcon);


            favoritesContainer.getChildren().add(card);
        
        }

    }

    
    @Override
    public void refreshLanguage() {
        backButton.setText(LanguageService.text(
            "← Terug naar home",
            "← Back to home"
        ));
            

    }
}