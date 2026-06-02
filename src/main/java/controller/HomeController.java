package controller;

import Service.LanguageService;
import Service.TripSearchState;
import Service.TripService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;

public class HomeController implements LanguageRefreshable {

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label messageLabel;

    @FXML
    private Label planTitleLabel;

    @FXML
    private Label fromTitleLabel;

    @FXML
    private Label toTitleLabel;

    @FXML
    private Label dateTimeLabel;

    @FXML
    private Button searchButton;

    @FXML
    private Spinner<Integer> hourSpinner;

    @FXML
    private ComboBox<String> fromComboBox;

    @FXML
    private ComboBox<String> toComboBox;

    @FXML
    private Spinner<Integer> minuteSpinner;

    private final TripService tripService = new TripService();

    private ObservableList<String> allStations;

    @FXML
    private void initialize() {
        allStations = FXCollections.observableArrayList(tripService.getStations());

        fromComboBox.setItems(FXCollections.observableArrayList(allStations));
        toComboBox.setItems(FXCollections.observableArrayList(allStations));

        setupAutoComplete(fromComboBox);
        setupAutoComplete(toComboBox);

        setupTimeSpinners();
        refreshLanguage();
    }

    private void setupAutoComplete(ComboBox<String> comboBox) {
        comboBox.setEditable(true);

        comboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isBlank()) {
                comboBox.setItems(FXCollections.observableArrayList(allStations));
                return;
            }

            String typedText = newValue.toLowerCase();
            ObservableList<String> filteredStations = FXCollections.observableArrayList();

            for (String station : allStations) {
                if (station.toLowerCase().contains(typedText)) {
                    filteredStations.add(station);
                }
            }

            comboBox.setItems(filteredStations);

            if (!comboBox.isShowing()) {
                comboBox.show();
            }
        });

        comboBox.setOnAction(event -> {
            String selectedStation = comboBox.getSelectionModel().getSelectedItem();

            if (selectedStation != null) {
                comboBox.getEditor().setText(selectedStation);
            }
        });

        comboBox.getEditor().setOnAction(event -> {
            if (!comboBox.getItems().isEmpty()) {
                String firstMatch = comboBox.getItems().get(0);
                comboBox.setValue(firstMatch);
                comboBox.getEditor().setText(firstMatch);
                comboBox.hide();
            }
        });
    }

    private void setupTimeSpinners() {
        SpinnerValueFactory.IntegerSpinnerValueFactory hourFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12);

        SpinnerValueFactory.IntegerSpinnerValueFactory minuteFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);

        StringConverter<Integer> twoDigitConverter = new StringConverter<>() {
            @Override
            public String toString(Integer value) {
                if (value == null) {
                    return "00";
                }

                return String.format("%02d", value);
            }

            @Override
            public Integer fromString(String text) {
                if (text == null || text.isBlank()) {
                    return 0;
                }

                int value = Integer.parseInt(text);

                if (value < 0) {
                    return 0;
                }

                if (value > 59) {
                    return 59;
                }

                return value;
            }
        };

        hourFactory.setConverter(twoDigitConverter);
        minuteFactory.setConverter(twoDigitConverter);

        hourSpinner.setValueFactory(hourFactory);
        minuteSpinner.setValueFactory(minuteFactory);

        hourSpinner.setEditable(false);
        minuteSpinner.setEditable(false);
    }

    @Override
    public void refreshLanguage() {
        planTitleLabel.setText(LanguageService.text("Plan je reis", "Plan your trip"));
        fromTitleLabel.setText(LanguageService.text("Van", "From"));
        toTitleLabel.setText(LanguageService.text("Naar", "To"));
        dateTimeLabel.setText(LanguageService.text("Datum en tijd", "Date and time"));

        searchButton.setText(LanguageService.text("🔍  Zoek reis", "🔍  Search trip"));

        fromComboBox.setPromptText(LanguageService.text(
                "Kies vertrekstation",
                "Choose departure station"
        ));

        toComboBox.setPromptText(LanguageService.text(
                "Kies aankomststation",
                "Choose arrival station"
        ));

        if (messageLabel.getText() != null && !messageLabel.getText().isEmpty()) {
            messageLabel.setText(getChooseStationsMessage());
        }
    }

    private String getChooseStationsMessage() {
        return Language