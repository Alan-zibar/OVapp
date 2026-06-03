package controller;

import Service.LanguageService;
import Service.TripSearchState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import model.Trip;
import model.Trip.TripStop;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    private final List<Trip> trainTrips = new ArrayList<>();
    private final List<Trip> busTrips = new ArrayList<>();
    private final List<Trip> mixedTrips = new ArrayList<>();
    private String selectedTransport = "ALL";
    private int visibleResults = 2;

    @FXML
    private void initialize() {
        setupTransportButtons();
        loadSearchSummary();
        refreshLanguage();
    }

    private void setupTransportButtons() {
        trainToggleButton.setSelected(false);
        busToggleButton.setSelected(false);

        trainToggleButton.setOnAction(event -> {
            selectedTransport = trainToggleButton.isSelected() ? "TRAIN" : "ALL";
            busToggleButton.setSelected(false);
            visibleResults = 2;
            renderResults();
        });

        busToggleButton.setOnAction(event -> {
            selectedTransport = busToggleButton.isSelected() ? "BUS" : "ALL";
            trainToggleButton.setSelected(false);
            visibleResults = 2;
            renderResults();
        });
    }

    @Override
    public void refreshLanguage() {
        resultsTitleLabel.setText(LanguageService.text("Kies een reisoptie", "Choose a trip option"));
        fromLabel.setText(LanguageService.text("Van:", "From:"));
        toLabel.setText(LanguageService.text("Naar:", "To:"));
        dateTimeLabel.setText(LanguageService.text("Datum en tijd:", "Date and time:"));
        changeSearchButton.setText(LanguageService.text("Wijzigen", "Change"));
        transportTitleLabel.setText(LanguageService.text("Kies een reismiddel", "Choose transport"));
        trainToggleButton.setText(LanguageService.text("\uD83D\uDE86  Trein", "\uD83D\uDE86  Train"));
        busToggleButton.setText(LanguageService.text("\uD83D\uDE8C  Bus", "\uD83D\uDE8C  Bus"));
        optionsTitleLabel.setText(LanguageService.text("Kies een reisoptie", "Choose a trip option"));
        loadMoreButton.setText(LanguageService.text("Meer resultaten laden\u2304", "Load more results\u2304"));

        createHardcodedTrips();
        renderResults();
    }

    private void loadSearchSummary() {
        String fromStation = TripSearchState.getFromStation();
        String toStation = TripSearchState.getToStation();

        fromValueLabel.setText(valueOrDash(fromStation));
        toValueLabel.setText(valueOrDash(toStation));

        String date = TripSearchState.getSelectedDate();
        String time = TripSearchState.getSelectedTime();

        if ((date == null || date.isBlank()) && (time == null || time.isBlank())) {
            dateTimeValueLabel.setText("-");
        } else {
            dateTimeValueLabel.setText(valueOrDash(date) + " " + valueOrDash(time));
        }

        if (fromStation != null && !fromStation.isBlank() && toStation != null && !toStation.isBlank()) {
            Service.HistoryService.getInstance().addEntry(fromStation, toStation, selectedTransport);
        }
    }

    private void createHardcodedTrips() {
        trainTrips.clear();
        busTrips.clear();
        mixedTrips.clear();

        String from = valueOrDash(TripSearchState.getFromStation());
        String to = valueOrDash(TripSearchState.getToStation());
        LocalTime selectedTime = parseSelectedTime();
        int duration = calculateFakeDuration(from, to);

        trainTrips.add(createTrip(selectedTime, duration, 0, 10, from, to,
                "TRAIN", "Intercity", "Spoor 7", "Spoor 12"));
        trainTrips.add(createTrip(selectedTime, duration, 25, 0, from, to,
                "TRAIN", "Sprinter", "Spoor 3", "Spoor 12"));
        trainTrips.add(createTrip(selectedTime, duration, 45, 0, from, to,
                "TRAIN", "Intercity direct", "Spoor 5", "Spoor 12"));
        trainTrips.add(createTrip(selectedTime, duration, 70, 5, from, to,
                "TRAIN", "Sprinter", "Spoor 2", "Spoor 12"));

        busTrips.add(createTrip(selectedTime, duration + 10, 10, 0, from, to,
                "BUS", "Lijn 202", "Bushalte B2", "Bushalte"));
        busTrips.add(createTrip(selectedTime, duration + 15, 30, 5, from, to,
                "BUS", "Lijn 382", "Bushalte C1", "Bushalte"));
        busTrips.add(createTrip(selectedTime, duration + 15, 55, 0, from, to,
                "BUS", "Lijn 70", "Bushalte A4", "Bushalte"));
        busTrips.add(createTrip(selectedTime, duration + 15, 80, 15, from, to,
                "BUS", "Lijn 56", "Bushalte D3", "Bushalte"));

        for (int index = 0; index < trainTrips.size(); index++) {
            mixedTrips.add(trainTrips.get(index));
            mixedTrips.add(busTrips.get(index));
        }
    }

    private Trip createTrip(LocalTime baseTime,
                            int durationMinutes,
                            int departureOffset,
                            int delayMinutes,
                            String from,
                            String to,
                            String transportMode,
                            String transportType,
                            String departureLocation,
                            String arrivalLocation) {
        LocalTime departure = baseTime.plusMinutes(departureOffset);
        LocalTime scheduledArrival = departure.plusMinutes(durationMinutes);
        LocalTime expectedArrival = scheduledArrival.plusMinutes(delayMinutes);
        LocalTime intermediate = departure.plusMinutes(durationMinutes / 2);

        return new Trip(
                formatTime(departure),
                formatTime(scheduledArrival),
                formatTime(expectedArrival),
                from,
                to,
                transportMode,
                transportType,
                departureLocation,
                arrivalLocation,
                delayMinutes,
                durationMinutes,
                durationMinutes,
                0,
                List.of(
                        new TripStop(formatTime(departure), from, "DEPARTURE", departureLocation),
                        new TripStop(formatTime(intermediate), LanguageService.text("Tussenstation", "Intermediate stop"),
                                "INTERMEDIATE", ""),
                        new TripStop(formatTime(expectedArrival), to, "ARRIVAL", arrivalLocation)
                )
        );
    }

    private void renderResults() {
        resultsContainer.getChildren().clear();
        List<Trip> selectedTrips = selectedTrips();
        int max = Math.min(visibleResults, selectedTrips.size());

        for (int index = 0; index < max; index++) {
            resultsContainer.getChildren().add(loadResultCard(selectedTrips.get(index)));
        }

        boolean hasMoreResults = visibleResults < selectedTrips.size();
        loadMoreButton.setVisible(hasMoreResults);
        loadMoreButton.setManaged(hasMoreResults);
    }

    private List<Trip> selectedTrips() {
        return switch (selectedTransport) {
            case "BUS" -> busTrips;
            case "TRAIN" -> trainTrips;
            default -> mixedTrips;
        };
    }

    private VBox loadResultCard(Trip trip) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/ovapp/view/result-card.fxml"
            ));
            VBox card = loader.load();
            ResultCardController controller = loader.getController();
            controller.setTrip(trip);
            return card;
        } catch (IOException exception) {
            throw new IllegalStateException("Resultaatkaart kon niet worden geladen", exception);
        }
    }

    private LocalTime parseSelectedTime() {
        String time = TripSearchState.getSelectedTime();

        if (time == null || time.isBlank()) {
            return LocalTime.of(10, 0);
        }

        return LocalTime.parse(time, TIME_FORMATTER);
    }

    private String formatTime(LocalTime time) {
        return time.format(TIME_FORMATTER);
    }

    private int calculateFakeDuration(String from, String to) {
        int hash = Math.abs((from + to).hashCode());
        return 30 + (hash % 35);
    }

    private String valueOrDash(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    @FXML
    private void loadMoreResults(ActionEvent event) {
        visibleResults += 2;
        renderResults();
    }

    @FXML
    private void goBackToSearch(ActionEvent event) {
        MainLayoutController.loadPage("home.fxml");
    }
}
