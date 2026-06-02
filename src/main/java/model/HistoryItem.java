package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryItem {
    private String fromStation;
    private String toStation;
    private String departureTime;
    private String type; // TRAIN_STATION or BUS_STOP

    public HistoryItem(String fromStation, String toStation, String type) {
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.type = type;
        this.departureTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public String getFromStation() { return fromStation; }
    public String getToStation() { return toStation; }
    public String getDepartureTime() { return departureTime; }
    public String getType() { return type; }
}
