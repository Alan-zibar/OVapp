package Service;

import java.util.List;

public class TripService {

    public List<String> getStations() {
        return List.of(
                "Amersfoort Centraal",
                "Utrecht Centraal",
                "Amsterdam Centraal",
                "Rotterdam Centraal",
                "Den Haag Centraal"
        );
    }

    public boolean isValidTrip(String from, String to) {

        // Als er geen vertrekstation is gekozen
        if (from == null) {
            return false;
        }
        // Als er geen aankomststation is gekozen
        if (to == null) {
            return false;
        }
        // Als vertrekstation leeg is
        if (from.isBlank()) {
            return false;
        }
        // Als aankomststation leeg is
        if (to.isBlank()) {
            return false;
        }
        // Als vertrekstation en aankomststation hetzelfde zijn
        if (from.equals(to)) {
            return false;
        }
        // Als alles goed is, is de reis geldig
        return true;
    }
}
