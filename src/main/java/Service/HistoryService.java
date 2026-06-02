package Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HistoryService {
    private static final HistoryService instance = new HistoryService();
    private final ObservableList<String> historyList = FXCollections.observableArrayList();

    private HistoryService() {}

    public static HistoryService getInstance() {
        return instance;
    }

    public void addEntry(String from, String to, String transportType) {
        String typeText = transportType.equalsIgnoreCase("BUS") ? "Bus | Lijn" : "Trein | Intercity";

        java.time.LocalDate today = java.time.LocalDate.now();
        String dateText = String.format("%02d-%02d-%d", today.getDayOfMonth(), today.getMonthValue(), today.getYear());

        String entry = from + "|" + to + "|" + typeText + "|" + dateText;

        if (historyList.isEmpty() || !historyList.get(0).equals(entry)) {
            historyList.add(0, entry);
        }
    }
    public void clearHistory() {
        this.historyList.clear();
    }

    public ObservableList<String> getHistoryList() {
        return historyList;
    }
}
