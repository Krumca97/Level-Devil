package projekt.frontEnd.history;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import projekt.backEnd.entities.GameRecord;
import projekt.backEnd.entities.Player;
import projekt.frontEnd.ApiClient;
import projekt.frontEnd.MenuController;

import java.util.List;

public class HistoryController {
    @FXML
    private ListView<String> listView;

    @FXML
    public void initialize() {
        Player player = MenuController.getCurrentPlayer();
        if (player == null) {
            return;
        }
        List<GameRecord> records = ApiClient.getGameRecordsByPlayerId(player.getId());
        if (records == null) {
            return;
        }
        records.stream()
                .limit(10)
                .forEach(r -> listView.getItems().add(
                        player.getName() + " | Level " + r.getLevel() + " | " + formatTime(r.getTime())
                ));
    }

    private String formatTime(long ms) {
        long totalSeconds = ms / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return minutes + ":" + String.format("%02d", seconds);
    }
}
