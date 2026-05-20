package controller;

import Service.LanguageService;
import Service.TripSearchState;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Trip;
import model.Trip.TripStop;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class RouteDetailController implements LanguageRefreshable {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @FXML
    private Button backButton;
    @FXML
    private Button favoriteButton;
    @FXML
    private Label transportIconLabel;
    @FXML
    private Label timeRangeLabel;
    @FXML
    private Label routeNameLabel;
    @FXML
    private Label transportInfoLabel;
    @FXML
    private Label dateTimeSummaryLabel;
    @FXML
    private Label plannedArrivalLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label expectedArrivalLabel;
    @FXML
    private Label routeOverviewTitleLabel;
    @FXML
    private Label detailedTransportLabel;
    @FXML
    private VBox stopsContainer;
    @FXML
    private Label durationValueLabel;
    @FXML
    private Label durationTitleLabel;
    @FXML
    private Label distanceValueLabel;
    @FXML
    private Label distanceTitleLabel;
    @FXML
    private Label transfersValueLabel;
    @FXML
    private Label transfersTitleLabel;
    @FXML
    private Button travelDataButton;
    @FXML
    private HBox favoriteMessageBox;
    @FXML
    private Label favoriteMessageLabel;

    @FXML
    private void initialize() {
        refreshLanguage();
    }

    @Override
    public void refreshLanguage() {
        Trip trip = currentTrip();

        backButton.setText("\u2190  " + LanguageService.text("Terug naar resultaten", "Back to results"));
        favoriteButton.setText("\u2606  " + LanguageService.text("Opslaan als favoriet", "Save as favorite"));
        routeOverviewTitleLabel.setText(LanguageService.text("Routeverloop", "Route overview"));
        durationTitleLabel.setText(LanguageService.text("Reistijd", "Travel time"));
        distanceTitleLabel.setText(LanguageService.text("Afstand", "Distance"));
        transfersTitleLabel.setText(LanguageService.text("Overstappen", "Transfers"));
        travelDataButton.setText(LanguageService.text("Gegevens tijdens de reis  >", "Journey information  >"));
        favoriteMessageLabel.setText(LanguageService.text("Toegevoegd aan favorieten", "Added to favorites"));

        transportIconLabel.setText("BUS".equals(trip.getTransportMode()) ? "\uD83D\uDE8C" : "\uD83D\uDE86");
        timeRangeLabel.setText(trip.getDepartureTime() + "  ->  " + trip.getExpectedArrivalTime());
        routeNameLabel.setText(trip.getFromStation() + "  ->  " + trip.getToStation());
        transportInfoLabel.setText(transportSummary(trip));
        dateTimeSummaryLabel.setText(formatSelectedDateTime());
        statusLabel.setText(statusText(trip));
        expectedArrivalLabel.setText(LanguageService.text(
                "Verwacht: " + trip.getExpectedArrivalTime(),
                "Expected: " + trip.getExpectedArrivalTime()
        ));

        if (trip.getDelayMinutes() > 0) {
            plannedArrivalLabel.setText(LanguageService.text(
                    "Gepland: " + trip.getScheduledArrivalTime(),
                    "Planned: " + trip.getScheduledArrivalTime()
            ));
        } else {
            plannedArrivalLabel.setText(LanguageService.text(
                    "Aankomst: " + trip.getExpectedArrivalTime(),
                    "Arrival: " + trip.getExpectedArrivalTime()
            ));
        }

        detailedTransportLabel.setText(transportName(trip) + " " + trip.getTransportType());
        durationValueLabel.setText(trip.getDurationMinutes() + " min");
        distanceValueLabel.setText(trip.getDistanceKm() + " km");
        transfersValueLabel.setText(String.valueOf(trip.getTransfers()));

        renderStops(trip);
    }

    private Trip currentTrip() {
        Trip trip = TripSearchState.getSelectedTrip();

        if (trip != null) {
            return trip;
        }

        return fallbackTrip();
    }

    private Trip fallbackTrip() {
        String from = valueOrDash(TripSearchState.getFromStation());
        String to = valueOrDash(TripSearchState.getToStation());
        LocalTime departure = parseSelectedTime();
        String departureTime = departure.format(TIME_FORMATTER);
        String arrivalTime = departure.plusMinutes(45).format(TIME_FORMATTER);

        return new Trip(
                departureTime,
                arrivalTime,
                arrivalTime,
                from,
                to,
                "TRAIN",
                "Intercity",
                "Spoor 7",
                "Spoor 12",
                0,
                45,
                45,
                0,
                List.of(
                        new TripStop(departureTime, from, "DEPARTURE", "Spoor 7"),
                        new TripStop(departure.plusMinutes(22).format(TIME_FORMATTER), "Tussenstation", "INTERMEDIATE", ""),
                        new TripStop(arrivalTime, to, "ARRIVAL", "Spoor 12")
                )
        );
    }

    private void renderStops(Trip trip) {
        stopsContainer.getChildren().clear();
        List<TripStop> stops = trip.getStops();

        for (int i = 0; i < stops.size(); i++) {
            TripStop stop = stops.get(i);
            HBox row = new HBox(16);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setMinHeight(72);
            row.setPrefHeight(72);

            Label timeLabel = new Label(stop.getTime());
            timeLabel.setPrefWidth(58);
            timeLabel.setStyle("-fx-font-size: 15px;");

            StackPane marker = createTimelineMarker(i == 0, i == stops.size() - 1);

            VBox stopText = new VBox(5);
            HBox.setHgrow(stopText, Priority.ALWAYS);

            Label stationLabel = new Label(stop.getStationName());
            stationLabel.setWrapText(true);
            stationLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Label detailLabel = new Label(stopDetail(stop));
            detailLabel.setWrapText(true);
            detailLabel.setStyle("-fx-font-size: 14px;");

            stopText.getChildren().addAll(stationLabel, detailLabel);
            row.getChildren().addAll(timeLabel, marker, stopText);

            stopsContainer.getChildren().add(row);
        }
    }

    private StackPane createTimelineMarker(boolean first, boolean last) {
        StackPane marker = new StackPane();
        marker.setPrefWidth(28);
        marker.setMinHeight(72);
        marker.setPrefHeight(72);
        marker.setMaxHeight(72);

        Region line = new Region();
        line.setPrefWidth(2);
        line.setMaxWidth(2);
        line.setStyle("-fx-background-color: #333333;");

        if (first && last) {
            line.setPrefHeight(0);
        } else if (first) {
            line.setPrefHeight(36);
            StackPane.setAlignment(line, Pos.BOTTOM_CENTER);
        } else if (last) {
            line.setPrefHeight(36);
            StackPane.setAlignment(line, Pos.TOP_CENTER);
        } else {
            line.setPrefHeight(72);
            StackPane.setAlignment(line, Pos.CENTER);
        }

        Circle circle = new Circle(5);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.web("#333333"));
        circle.setStrokeWidth(1.8);
        StackPane.setAlignment(circle, Pos.CENTER);

        marker.getChildren().addAll(line, circle);
        return marker;
    }

    private String stopDetail(TripStop stop) {
        String stopType = switch (stop.getStopType()) {
            case "DEPARTURE" -> LanguageService.text("Vertrek", "Departure");
            case "ARRIVAL" -> LanguageService.text("Aankomst", "Arrival");
            default -> LanguageService.text("Tussenstop", "Intermediate stop");
        };

        if (stop.getLocation() == null || stop.getLocation().isBlank()) {
            return stopType;
        }

        return stopType + " | " + localizedLocation(stop.getLocation());
    }

    private String statusText(Trip trip) {
        if (trip.getDelayMinutes() <= 0) {
            return LanguageService.text("Op tijd", "On time");
        }

        return LanguageService.text(
                "+" + trip.getDelayMinutes() + " min vertraging",
                "+" + trip.getDelayMinutes() + " min delay"
        );
    }

    private String transportSummary(Trip trip) {
        return transportName(trip) + " | " + trip.getTransportType() + " | " + localizedLocation(trip.getDepartureLocation());
    }

    private String transportName(Trip trip) {
        return "BUS".equals(trip.getTransportMode())
                ? LanguageService.text("Bus", "Bus")
                : LanguageService.text("Trein", "Train");
    }

    private String localizedLocation(String location) {
        if (location.startsWith("Spoor ")) {
            return LanguageService.text(location, "Platform " + location.substring("Spoor ".length()));
        }

        if (location.startsWith("Bushalte ")) {
            return LanguageService.text(location, "Bus stop " + location.substring("Bushalte ".length()));
        }

        return location;
    }

    private String formatSelectedDateTime() {
        String date = TripSearchState.getSelectedDate();
        String time = valueOrDash(TripSearchState.getSelectedTime());

        if (date == null || date.isBlank()) {
            return LanguageService.text("Geen datum gekozen", "No date selected") + " " + time;
        }

        try {
            return LocalDate.parse(date).format(DATE_FORMATTER) + " " + time;
        } catch (DateTimeParseException exception) {
            return date + " " + time;
        }
    }

    private LocalTime parseSelectedTime() {
        String time = TripSearchState.getSelectedTime();

        if (time == null || time.isBlank()) {
            return LocalTime.of(10, 0);
        }

        try {
            return LocalTime.parse(time, TIME_FORMATTER);
        } catch (DateTimeParseException exception) {
            return LocalTime.of(10, 0);
        }
    }

    private String valueOrDash(String value) {
        if (value == null || value.isBlank()) {
            return "-";
        }

        return value;
    }

    @FXML
    private void goBackToResults() {
        MainLayoutController.loadPage("results.fxml");
    }

    @FXML
    private void addToFavorites() {
        favoriteMessageBox.setVisible(true);
        favoriteMessageBox.setManaged(true);
        favoriteButton.setText("\u2605  " + LanguageService.text("Opgeslagen als favoriet", "Saved as favorite"));
    }

    @FXML
    private void closeFavoriteMessage() {
        favoriteMessageBox.setVisible(false);
        favoriteMessageBox.setManaged(false);
    }
}
