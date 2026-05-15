package Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HistoryService {
    private static final HistoryService instance = new HistoryService();
    private final ObservableList<String> historyList = FXCollections.observableArrayList();

    private HistoryService() {}

    public static HistoryService getInstance() {
        return instance;
    }

    public void addEntry(String from, String to, String transportType) {

        String icon = transportType.equalsIgnoreCase("BUS") ? "Bus" : "Trein";
        String entry = String.format("%s %s ➔ %s", icon, from, to);


        if (historyList.isEmpty() || !historyList.get(0).equals(entry)) {
            historyList.add(0, entry);
        }
    }

    public ObservableList<String> getHistoryList() {
        return historyList;
    }
}
