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
import Service.SessionService;
import javafx.scene.control.ScrollPane;


public class FavoritesController implements LanguageRefreshable {

    @FXML
    private void backToHome() {
        MainLayoutController.loadPage("home.fxml");
    }


    @FXML
    private VBox favoritesContainer;

    @FXML
    private Button backButton;

    @FXML
    private Label titleLabel;


    @FXML
    private ScrollPane favoritesScrollPane;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private HBox loginMessageBox;

    @FXML
    private Button clearFavoritesButton;

    @FXML
    private void initialize() {

        backButton.setText(LanguageService.text(
                "← Terug naar home",
                "← Back to home"));

        clearFavoritesButton.setText(LanguageService.text(
                "Verwijder alle favorieten",
                "Clear all favorites"
        ));

        titleLabel.setText(LanguageService.text(
                "Mijn Favorieten",
                "My Favorites"));

        favoritesContainer.getChildren().clear();

        if (!SessionService.isLoggedIn()) {
            loginMessageLabel.setText(LanguageService.text(
                    "Log in om je favorieten te bekijken.",
                    "Log in to view your favorites."
            ));

            loginMessageBox.setVisible(true);
            loginMessageBox.setManaged(true);

            favoritesScrollPane.setVisible(false);
            favoritesScrollPane.setManaged(false);

            clearFavoritesButton.setVisible(false);
            clearFavoritesButton.setManaged(false);

            return;
        }

        loginMessageBox.setVisible(false);
        loginMessageBox.setManaged(false);

        favoritesScrollPane.setVisible(true);
        favoritesScrollPane.setManaged(true);

        clearFavoritesButton.setVisible(true);
        clearFavoritesButton.setManaged(true);

        if (FavoriteService.getFavorites().isEmpty()) {
            clearFavoritesButton.setVisible(false);
            clearFavoritesButton.setManaged(false);

            Label emptyMessage = new Label(LanguageService.text(
                    "Je hebt nog geen favorieten.",
                    "You do not have any favorites yet."
            ));

            emptyMessage.setStyle("-fx-font-size: 16px; -fx-text-fill: #777777; -fx-padding: 80 0 0 0;");
            favoritesContainer.setAlignment(Pos.TOP_CENTER);
            favoritesContainer.getChildren().add(emptyMessage);

            favoritesScrollPane.setVvalue(0);

            return;
        }

        favoritesContainer.setAlignment(Pos.TOP_CENTER);

            for (model.Trip trip : FavoriteService.getFavorites()) {
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

                Label iconLabel = new Label("📍");
                iconLabel.setStyle("-fx-font-size: 30px;");

                Label tripLabel = new Label(trip.getFromStation() + " → " + trip.getToStation());
                Label detailsLabel = new Label(
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

    @FXML
    private void clearAllFavorites() {
        FavoriteService.getFavorites().clear();
        favoritesContainer.getChildren().clear();

        Label emptyMessage = new Label(LanguageService.text(
                "Je hebt nog geen favorieten.",
                "You do not have any favorites yet."
        ));

        emptyMessage.setStyle("-fx-font-size: 16px; -fx-text-fill: #777777; -fx-padding: 80 0 0 0;");
        favoritesContainer.setAlignment(Pos.TOP_CENTER);
        favoritesContainer.getChildren().add(emptyMessage);

        favoritesScrollPane.setVvalue(0);
    }

    @Override
    public void refreshLanguage() {
        backButton.setText(LanguageService.text(
                "← Terug naar home",
                "← Back to home"
        ));

        titleLabel.setText(LanguageService.text(
                "Mijn Favorieten",
                "My Favorites"
        ));

        if (loginMessageLabel != null) {
            loginMessageLabel.setText(LanguageService.text(
                    "Log in om je favorieten te bekijken.",
                    "Log in to view your favorites."
            ));
        }

        clearFavoritesButton.setText(LanguageService.text(
                "Verwijder alle favorieten",
                "Clear all favorites"
        ));
    }
        }



