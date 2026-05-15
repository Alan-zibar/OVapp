package controller;

import Service.HistoryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class HistoryController {
    @FXML
    private ListView<String> historyListView;

    public void initialize() {
        // Service içindeki listeyi ListView'a bağla
        historyListView.setItems(HistoryService.getInstance().getHistoryList());
    }

    public void onHistoryClick(ActionEvent actionEvent) {
    }
}
