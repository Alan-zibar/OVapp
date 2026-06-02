package controller;

import Service.LanguageService;
import Service.TripSearchState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class ResultsController implements LanguageRefreshable {

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

    private final List<HBox> trainResultCards = new ArrayList<>();
    private final List<HBox> busResultCards = new ArrayList<>();
    private String selectedTransport = "TRAIN";
    private int visibleResults = 2;



    @FXML
    private void initialize() {
        refreshLanguage();
        loadSearchSummary();
        createHardcodedResults();
        setupTransportButtons();
        renderResults();
    }


    private void setupTransportButtons() {
        trainToggleButton.setSelected(true);
        busToggleButton.setSelected(false);

        trainToggleButton.setOnAction(event -> {
            selectedTransport = "TRAIN";
            trainToggleButton.setSelected(true);
            busToggleButton.setSelected(false);
            visibleResults = 2;
            renderResults();
        });

        busToggleButton.setOnAction(event -> {
            selectedTransport = "BUS";
            trainToggleButton.setSelected(false);
            busToggleButton.setSelected(true);
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
        trainToggleButton.setText(LanguageService.text("🚆  Trein", "🚆  Train"));
        busToggleButton.setText(LanguageService.text("🚌  Bus", "🚌  Bus"));
        optionsTitleLabel.setText(LanguageService.text("Kies een reisoptie", "Choose a trip option"));
        loadMoreButton.setText(LanguageService.text("Meer resultaten laden⌄", "Load more results⌄"));

        createHardcodedResults();
        renderResults();
    }

    private void loadSearchSummary() {

        String fromStation = TripSearchState.getFromStation();
        String toStation = TripSearchState.getToStation();

        fromValueLabel.setText(valueOrDash(TripSearchState.getFromStation()));
        toValueLabel.setText(valueOrDash(TripSearchState.getToStation()));

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

    private String valueOrDash(String value) {
        if (value == null || value.isBlank()) {
            return "-";
        }

        return value;
    }

    private void createHardcodedResults() {
        trainResultCards.clear();
        busResultCards.clear();

        String from = valueOrDash(TripSearchState.getFromStation());
        String to = valueOrDash(TripSearchState.getToStation());

        LocalTime selectedTime = parseSelectedTime();
        int duration = calculateFakeDuration(from, to);

        String trainDeparture1 = formatTime(selectedTime);
        String trainArrival1 = formatTime(selectedTime.plusMinutes(duration));

        String trainDeparture2 = formatTime(selectedTime.plusMinutes(25));
        String trainArrival2 = formatTime(selectedTime.plusMinutes(duration + 25));

        String trainDeparture3 = formatTime(selectedTime.plusMinutes(45));
        String trainArrival3 = formatTime(selectedTime.plusMinutes(duration + 45));

        String trainDeparture4 = formatTime(selectedTime.plusMinutes(70));
        String trainArrival4 = formatTime(selectedTime.plusMinutes(duration + 70));

        String busDeparture1 = formatTime(selectedTime.plusMinutes(10));
        String busArrival1 = formatTime(selectedTime.plusMinutes(duration + 20));

        String busDeparture2 = formatTime(selectedTime.plusMinutes(30));
        String busArrival2 = formatTime(selectedTime.plusMinutes(duration + 45));

        String busDeparture3 = formatTime(selectedTime.plusMinutes(55));
        String busArrival3 = formatTime(selectedTime.plusMinutes(duration + 70));

        String busDeparture4 = formatTime(selectedTime.plusMinutes(80));
        String busArrival4 = formatTime(selectedTime.plusMinutes(duration + 95));

        trainResultCards.add(createResultCard(
                trainDeparture1,
                trainArrival1,
                from,
                to,
                LanguageService.text("Trein | Intercity | Spoor 7", "Train | Intercity | Platform 7"),
                LanguageService.text("+10 min vertraging", "+10 min delay"),
                LanguageService.text(
                        "Verwachte aankomst: " + formatTime(selectedTime.plusMinutes(duration + 10)),
                        "Expected arrival: " + formatTime(selectedTime.plusMinutes(duration + 10))
                )
        ));

        trainResultCards.add(createResultCard(
                trainDeparture2,
                trainArrival2,
                from,
                to,
                LanguageService.text("Trein | Sprinter | Spoor 3", "Train | Sprinter | Platform 3"),
                LanguageService.text("Op tijd", "On time"),
                LanguageService.text(
                        "Verwachte aankomst: " + trainArrival2,
                        "Expected arrival: " + trainArrival2
                )
        ));

        trainResultCards.add(createResultCard(
                trainDeparture3,
                trainArrival3,
                from,
                to,
                LanguageService.text("Trein | Intercity direct | Spoor 5", "Train | Intercity direct | Platform 5"),
                LanguageService.text("Op tijd", "On time"),
                LanguageService.text(
                        "Verwachte aankomst: " + trainArrival3,
                        "Expected arrival: " + trainArrival3
                )
        ));

        trainResultCards.add(createResultCard(
                trainDeparture4,
                trainArrival4,
                from,
                to,
                LanguageService.text("Trein | Sprinter | Spoor 2", "Train | Sprinter | Platform 2"),
                LanguageService.text("+5 min vertraging", "+5 min delay"),
                LanguageService.text(
                        "Verwachte aankomst: " + formatTime(selectedTime.plusMinutes(duration + 75)),
                        "Expected arrival: " + formatTime(selectedTime.plusMinutes(duration + 75))
                )
        ));

        busResultCards.add(createResultCard(
                busDeparture1,
                busArrival1,
                from,
                to,
                LanguageService.text("Bus | Lijn 202 | Bushalte B2", "Bus | Line 202 | Bus stop B2"),
                LanguageService.text("Op tijd", "On time"),
                LanguageService.text(
                        "Verwachte aankomst: " + busArrival1,
                        "Expected arrival: " + busArrival1
                )
        ));

        busResultCards.add(createResultCard(
                busDeparture2,
                busArrival2,
                from,
                to,
                LanguageService.text("Bus | Lijn 382 | Bushalte C1", "Bus | Line 382 | Bus stop C1"),
                LanguageService.text("+5 min vertraging", "+5 min delay"),
                LanguageService.text(
                        "Verwachte aankomst: " + formatTime(selectedTime.plusMinutes(duration + 50)),
                        "Expected arrival: " + formatTime(selectedTime.plusMinutes(duration + 50))
                )
        ));

        busResultCards.add(createResultCard(
                busDeparture3,
                busArrival3,
                from,
                to,
                LanguageService.text("Bus | Lijn 70 | Bushalte A4", "Bus | Line 70 | Bus stop A4"),
                LanguageService.text("Op tijd", "On time"),
                LanguageService.text(
                        "Verwachte aankomst: " + busArrival3,
                        "Expected arrival: " + busArrival3
                )
        ));

        busResultCards.add(createResultCard(
                busDeparture4,
                busArrival4,
                from,
                to,
                LanguageService.text("Bus | Lijn 56 | Bushalte D3", "Bus | Line 56 | Bus stop D3"),
                LanguageService.text("+15 min vertraging", "+15 min delay"),
                LanguageService.text(
                        "Verwachte aankomst: " + formatTime(selectedTime.plusMinutes(duration + 110)),
                        "Expected arrival: " + formatTime(selectedTime.plusMinutes(duration + 110))
                )
        ));
    }

    private LocalTime parseSelectedTime() {
        String time = TripSearchState.getSelectedTime();

        if (time == null || time.isBlank()) {
            return LocalTime.of(10, 0);
        }

        return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
    }

    private String formatTime(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private int calculateFakeDuration(String from, String to) {
        int hash = Math.abs((from + to).hashCode());
        return 30 + (hash % 35);
    }


    private HBox createResultCard(String departureTime,
                                  String arrivalTime,
                                  String from,
                                  String to,
                                  String transportInfo,
                                  String status,
                                  String expectedArrival) {

        Label iconLabel = new Label(transportInfo.contains("Bus") ? "🚌" : "🚆");
        iconLabel.setStyle("-fx-font-size: 42px;");

        Label timeLabel = new Label(departureTime + "  →  " + arrivalTime);
        timeLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label routeLabel = new Label(from + "  →  " + to);
        routeLabel.setStyle("-fx-font-size: 15px;");

        Label transportLabel = new Label(transportInfo);
        transportLabel.setStyle("-fx-font-size: 15px;");

        VBox leftInfo = new VBox(8, timeLabel, routeLabel, transportLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        Label statusLabel = new Label(status);
        statusLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label arrivalLabel = new Label(expectedArrival);
        arrivalLabel.setStyle("-fx-font-size: 15px;");

        Button viewRouteButton = new Button(LanguageService.text("Bekijk route", "View route"));
        viewRouteButton.setOnAction(event -> viewRoute());
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

    private void renderResults() {
        resultsContainer.getChildren().clear();

        List<HBox> selectedCards;

        if (selectedTransport.equals("BUS")) {
            selectedCards = busResultCards;
        } else {
            selectedCards = trainResultCards;
        }

        int max = Math.min(visibleResults, selectedCards.size());

        for (int i = 0; i < max; i++) {
            resultsContainer.getChildren().add(selectedCards.get(i));
        }

        boolean hasMoreResults = visibleResults < selectedCards.size();
        loadMoreButton.setVisible(hasMoreResults);
        loadMoreButton.setManaged(hasMoreResults);
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

    private void viewRoute() {
        MainLayoutController.loadPage("route-detail.fxml");
    }
}
