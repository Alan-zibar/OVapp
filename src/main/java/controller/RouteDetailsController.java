package controller;

import Service.LanguageService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Trip;
import model.Trip.TripStop;

import java.util.List;

public class RouteDetailsController {

    @FXML
    private Label departureTimeLabel;
    @FXML
    private Label departureStationLabel;
    @FXML
    private Label departureDetailLabel;
    @FXML
    private Label intermediateTimeLabel;
    @FXML
    private Label intermediateStationLabel;
    @FXML
    private Label intermediateDetailLabel;
    @FXML
    private Label arrivalTimeLabel;
    @FXML
    private Label arrivalStationLabel;
    @FXML
    private Label arrivalDetailLabel;
    @FXML
    private Label durationValueLabel;
    @FXML
    private Label durationTitleLabel;
    @FXML
    private Label transfersValueLabel;
    @FXML
    private Label transfersTitleLabel;

    public void setTrip(Trip trip) {
        List<TripStop> stops = trip.getStops();
        TripStop departure = stops.get(0);
        TripStop intermediate = stops.get(1);
        TripStop arrival = stops.get(2);

        setStop(departure, departureTimeLabel, departureStationLabel, departureDetailLabel);
        setStop(intermediate, intermediateTimeLabel, intermediateStationLabel, intermediateDetailLabel);
        setStop(arrival, arrivalTimeLabel, arrivalStationLabel, arrivalDetailLabel);

        durationValueLabel.setText(trip.getDurationMinutes() + " min");
        durationTitleLabel.setText(LanguageService.text("Reistijd", "Travel time"));
        transfersValueLabel.setText(String.valueOf(trip.getTransfers()));
        transfersTitleLabel.setText(LanguageService.text("Overstappen", "Transfers"));
    }

    private void setStop(TripStop stop, Label timeLabel, Label stationLabel, Label detailLabel) {
        timeLabel.setText(stop.getTime());
        stationLabel.setText(stop.getStationName());
        detailLabel.setText(stopDetail(stop));
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

    private String localizedLocation(String location) {
        if (location.startsWith("Spoor ")) {
            return LanguageService.text(location, "Platform " + location.substring("Spoor ".length()));
        }

        if (location.startsWith("Bushalte ")) {
            return LanguageService.text(location, "Bus stop " + location.substring("Bushalte ".length()));
        }

        return location;
    }
}
