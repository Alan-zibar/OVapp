package controller;

import Service.LanguageService;
import Service.TripService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;

public class HomeController implements LanguageRefreshable  {

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

    @FXML
    private void initialize() {
        fromComboBox.getItems().addAll(tripService.getStations());
        toComboBox.getItems().addAll(tripService.getStations());

        setupTimeSpinners();
        refreshLanguage();
    }

    private void setupTimeSpinners() {
        SpinnerValueFactory.IntegerSpinnerValueFactory hourFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12);

        SpinnerValueFactory.IntegerSpinnerValueFactory minuteFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);

        StringConverter<Integer> twoDigitConverter = new StringConverter<Integer>() {
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
        return LanguageService.text(
                "Kies een vertrekstation en aankomststation.",
                "Choose a departure station and arrival station."
        );
    }

    @FXML
    private void swapStations() {
        String from = fromComboBox.getValue();
        String to = toComboBox.getValue();

        fromComboBox.setValue(to);
        toComboBox.setValue(from);
    }

    @FXML
    private void searchTrip(ActionEvent event) {
        String from = fromComboBox.getValue();
        String to = toComboBox.getValue();

        int hour = hourSpinner.getValue();
        int minute = minuteSpinner.getValue();
        String selectedTime = String.format("%02d:%02d", hour, minute);

        if (!tripService.isValidTrip(from, to)) {
            messageLabel.setText(getChooseStationsMessage());
            return;
        }

        System.out.println("Gekozen tijd: " + selectedTime);
    }
}
