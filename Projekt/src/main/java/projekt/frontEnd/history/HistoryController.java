package projekt.frontEnd.history;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class HistoryController {
    @FXML
    private ListView<String> listView;

    @FXML
    public void initialize() {
        List<GameRecord> records = HistoryManager.load();
        records.stream().sorted().limit(10).forEach(r -> listView.getItems().add(r.getPlayerName() + " | Level " + r.getLevel() + " | " + formatTime(r.getTimeMs())));
    }

    private String formatTime(long ms) {
        long totalSeconds = ms / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return minutes + ":" + String.format("%02d", seconds);
    }
}
