package controller;

import Service.LanguageService;
import Service.TripSearchState;
import Service.TripService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Trip;
import model.Trip.TripStop;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ResultsController implements LanguageRefreshable {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    private Label resultsTitleLabel;
    @FXML
    private Label fromLabel;
    @FXML
    private Label fromValueLabel;
    @FXML
    private Label toLabel;
    @FXML
    private Label toValueLabel;
    @FXML
    private Label dateTimeLabel;
    @FXML
    private Label dateTimeValueLabel;
    @FXML
    private Button changeSearchButton;
    @FXML
    private Label transportTitleLabel;
    @FXML
    private ToggleButton trainToggleButton;
    @FXML
    private ToggleButton busToggleButton;
    @FXML
    private Label optionsTitleLabel;
    @FXML
    private VBox resultsContainer;
    @FXML
    private Button loadMoreButton;

    private final TripService tripService = new TripService();
    private final List<Trip> trainTrips = new ArrayList<>();
    private final List<Trip> busTrips = new ArrayList<>();
    private String selectedTransport = "TRAIN";
    private int visibleResults = 2;

    @FXML
    private void initialize() {
        selectedTransport = TripSearchState.getSelectedTransport();
        visibleResults = Math.max(2, TripSearchState.getVisibleResults());

        setupTransportButtons();
        loadSearchSummary();
        refreshLanguage();
    }

    private void setupTransportButtons() {
        updateTransportButtonState();

        trainToggleButton.setOnAction(event -> {
            selectedTransport = "TRAIN";
            visibleResults = 2;
            TripSearchState.saveResultsViewState(selectedTransport, visibleResults);
            updateTransportButtonState();
            renderResults();
        });

        busToggleButton.setOnAction(event -> {
            selectedTransport = "BUS";
            visibleResults = 2;
            TripSearchState.saveResultsViewState(selectedTransport, visibleResults);
            updateTransportButtonState();
            renderResults();
        });
    }

    private void updateTransportButtonState() {
        boolean busSelected = "BUS".equals(selectedTransport);
        trainToggleButton.setSelected(!busSelected);
        busToggleButton.setSelected(busSelected);
    }

    @Override
    public void refreshLanguage() {
        resultsTitleLabel.setText(LanguageService.text("Kies een reisoptie", "Choose a trip option"));
        fromLabel.setText(LanguageService.text("Van:", "From:"));
        toLabel.setText(LanguageService.text("Naar:", "To:"));
        dateTimeLabel.setText(LanguageService.text("Datum en tijd:", "Date and time:"));
        changeSearchButton.setText(LanguageService.text("Wijzigen", "Change"));
        transportTitleLabel.setText(LanguageService.text("Kies een reismiddel", "Choose transport"));
        trainToggleButton.setText("\uD83D\uDE86  " + LanguageService.text("Trein", "Train"));
        busToggleButton.setText("\uD83D\uDE8C  " + LanguageService.text("Bus", "Bus"));
        optionsTitleLabel.setText(LanguageService.text("Kies een reisoptie", "Choose a trip option"));
        loadMoreButton.setText(LanguageService.text("Meer resultaten laden", "Load more results"));

        createHardcodedResults();
        renderResults();
    }

    private void loadSearchSummary() {
        fromValueLabel.setText(valueOrDash(TripSearchState.getFromStation()));
        toValueLabel.setText(valueOrDash(TripSearchState.getToStation()));

        String date = TripSearchState.getSelectedDate();
        String time = TripSearchState.getSelectedTime();

        if ((date == null || date.isBlank()) && (time == null || time.isBlank())) {
            dateTimeValueLabel.setText("-");
        } else {
            dateTimeValueLabel.setText(valueOrDash(date) + " " + valueOrDash(time));
        }
    }

    private String valueOrDash(String value) {
        if (value == null || value.isBlank()) {
            return "-";
        }

        return value;
    }

    private void createHardcodedResults() {
        trainTrips.clear();
        busTrips.clear();

        String from = valueOrDash(TripSearchState.getFromStation());
        String to = valueOrDash(TripSearchState.getToStation());

        LocalTime selectedTime = parseSelectedTime();
        int duration = calculateFakeDuration(from, to);
        int distance = calculateFakeDistance(from, to);

        trainTrips.add(createTrip(
                formatTime(selectedTime),
                formatTime(selectedTime.plusMinutes(duration)),
                from,
                to,
                "TRAIN",
                "Intercity",
                "Spoor 7",
                "Spoor 12",
                10,
                duration,
                distance,
                0
        ));

        trainTrips.add(createTrip(
                formatTime(selectedTime.plusMinutes(25)),
                formatTime(selectedTime.plusMinutes(duration + 25)),
                from,
                to,
                "TRAIN",
                "Sprinter",
                "Spoor 3",
                "Spoor 8",
                0,
                duration,
                distance,
                0
        ));

        trainTrips.add(createTrip(
                formatTime(selectedTime.plusMinutes(45)),
                formatTime(selectedTime.plusMinutes(duration + 45)),
                from,
                to,
                "TRAIN",
                "Intercity direct",
                "Spoor 5",
                "Spoor 10",
                0,
                duration,
                distance,
                0
        ));

        trainTrips.add(createTrip(
                formatTime(selectedTime.plusMinutes(70)),
                formatTime(selectedTime.plusMinutes(duration + 70)),
                from,
                to,
                "TRAIN",
                "Sprinter",
                "Spoor 2",
                "Spoor 6",
                5,
                duration,
                distance,
                0
        ));

        busTrips.add(createTrip(
                formatTime(selectedTime.plusMinutes(10)),
                formatTime(selectedTime.plusMinutes(duration + 20)),
                from,
                to,
                "BUS",
                "Lijn 202",
                "Bushalte B2",
                "Bushalte A1",
                0,
                duration + 10,
                distance + 3,
                0
        ));

        busTrips.add(createTrip(
                formatTime(selectedTime.plusMinutes(30)),
                formatTime(selectedTime.plusMinutes(duration + 45)),
                from,
                to,
                "BUS",
                "Lijn 382",
                "Bushalte C1",
                "Bushalte D4",
                5,
                duration + 15,
                distance + 5,
                1
        ));

        busTrips.add(createTrip(
                formatTime(selectedTime.plusMinutes(55)),
                formatTime(selectedTime.plusMinutes(duration + 70)),
                from,
                to,
                "BUS",
                "Lijn 70",
                "Bushalte A4",
                "Bushalte C3",
                0,
                duration + 15,
                distance + 4,
                0
        ));

        busTrips.add(createTrip(
                formatTime(selectedTime.plusMinutes(80)),
                formatTime(selectedTime.plusMinutes(duration + 95)),
                from,
                to,
                "BUS",
                "Lijn 56",
                "Bushalte D3",
                "Bushalte B1",
                15,
                duration + 15,
                distance + 4,
                1
        ));
    }

    private Trip createTrip(String departureTime,
                            String scheduledArrivalTime,
                            String from,
                            String to,
                            String transportMode,
                            String transportType,
                            String departureLocation,
                            String arrivalLocation,
                            int delayMinutes,
                            int durationMinutes,
                            int distanceKm,
                            int transfers) {
        LocalTime departure = LocalTime.parse(departureTime, TIME_FORMATTER);
        LocalTime scheduledArrival = LocalTime.parse(scheduledArrivalTime, TIME_FORMATTER);
        String expectedArrivalTime = formatTime(scheduledArrival.plusMinutes(delayMinutes));
        String intermediateTime = formatTime(departure.plusMinutes(Math.max(5, durationMinutes / 2)));
        String intermediateStation = createIntermediateStation(transportMode, from, to);

        List<TripStop> stops = new ArrayList<>();
        stops.add(new TripStop(departureTime, from, "DEPARTURE", departureLocation));

        if (intermediateStation != null) {
            stops.add(new TripStop(intermediateTime, intermediateStation, "INTERMEDIATE", ""));
        }

        stops.add(new TripStop(expectedArrivalTime, to, "ARRIVAL", arrivalLocation));

        return new Trip(
                departureTime,
                scheduledArrivalTime,
                expectedArrivalTime,
                from,
                to,
                transportMode,
                transportType,
                departureLocation,
                arrivalLocation,
                delayMinutes,
                durationMinutes + delayMinutes,
                distanceKm,
                transfers,
                stops
        );
    }

    private String createIntermediateStation(String transportMode, String from, String to) {
        List<String> candidates = tripService.getStationsForTransportMode(transportMode);

        if (candidates.isEmpty()) {
            candidates = tripService.getStations();
        }

        if (candidates.isEmpty()) {
            return null;
        }

        int startIndex = Math.abs((from + to + transportMode).hashCode()) % candidates.size();

        for (int i = 0; i < candidates.size(); i++) {
            String candidate = candidates.get((startIndex + i) % candidates.size());

            if (!candidate.equals(from) && !candidate.equals(to)) {
                return candidate;
            }
        }

        return null;
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

    private String formatTime(LocalTime time) {
        return time.format(TIME_FORMATTER);
    }

    private int calculateFakeDuration(String from, String to) {
        int hash = Math.abs((from + to).hashCode());
        return 30 + (hash % 35);
    }

    private int calculateFakeDistance(String from, String to) {
        int hash = Math.abs((to + from).hashCode());
        return 25 + (hash % 90);
    }

    private HBox createResultCard(Trip trip) {
        Label iconLabel = new Label("BUS".equals(trip.getTransportMode()) ? "\uD83D\uDE8C" : "\uD83D\uDE86");
        iconLabel.setStyle("-fx-font-size: 42px;");

        Label timeLabel = new Label(trip.getDepartureTime() + "  ->  " + trip.getScheduledArrivalTime());
        timeLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label routeLabel = new Label(trip.getFromStation() + "  ->  " + trip.getToStation());
        routeLabel.setStyle("-fx-font-size: 15px;");

        Label transportLabel = new Label(transportSummary(trip));
        transportLabel.setStyle("-fx-font-size: 15px;");

        VBox leftInfo = new VBox(8, timeLabel, routeLabel, transportLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label statusLabel = new Label(statusText(trip));
        statusLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label arrivalLabel = new Label(LanguageService.text(
                "Verwachte aankomst: " + trip.getExpectedArrivalTime(),
                "Expected arrival: " + trip.getExpectedArrivalTime()
        ));
        arrivalLabel.setStyle("-fx-font-size: 15px;");

        Button viewRouteButton = new Button(LanguageService.text("Bekijk route", "View route"));
        viewRouteButton.setOnAction(event -> viewRoute(trip));
        viewRouteButton.setPrefWidth(130);
        viewRouteButton.setPrefHeight(40);
        viewRouteButton.setStyle("-fx-background-color: white; -fx-border-color: #777; -fx-font-size: 15px;");

        VBox rightInfo = new VBox(12, statusLabel, arrivalLabel, viewRouteButton);
        rightInfo.setStyle("-fx-border-color: transparent transparent transparent #cccccc; -fx-border-width: 0 0 0 1.5; -fx-padding: 0 0 0 25;");

        HBox card = new HBox(20, iconLabel, leftInfo, spacer, rightInfo);
        card.setStyle("-fx-border-color: #777; -fx-border-width: 1.5; -fx-padding: 16; -fx-background-color: white;");
        card.setPrefHeight(120);

        return card;
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
        String transport = "BUS".equals(trip.getTransportMode())
                ? LanguageService.text("Bus", "Bus")
                : LanguageService.text("Trein", "Train");

        return transport + " | " + trip.getTransportType() + " | " + localizedLocation(trip.getDepartureLocation());
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

    private void renderResults() {
        resultsContainer.getChildren().clear();

        List<Trip> selectedTrips = "BUS".equals(selectedTransport) ? busTrips : trainTrips;
        int max = Math.min(visibleResults, selectedTrips.size());

        for (int i = 0; i < max; i++) {
            resultsContainer.getChildren().add(createResultCard(selectedTrips.get(i)));
        }

        boolean hasMoreResults = visibleResults < selectedTrips.size();
        loadMoreButton.setVisible(hasMoreResults);
        loadMoreButton.setManaged(hasMoreResults);
    }

    @FXML
    private void loadMoreResults(ActionEvent event) {
        visibleResults += 2;
        TripSearchState.saveResultsViewState(selectedTransport, visibleResults);
        renderResults();
    }

    @FXML
    private void goBackToSearch(ActionEvent event) {
        MainLayoutController.loadPage("home.fxml");
    }

    private void viewRoute(Trip trip) {
        TripSearchState.saveResultsViewState(selectedTransport, visibleResults);
        TripSearchState.saveSelectedTrip(trip);
        MainLayoutController.loadPage("route-detail.fxml");
    }
}
