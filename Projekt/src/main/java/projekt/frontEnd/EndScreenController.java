package projekt.frontEnd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class EndScreenController {
    @FXML
    private Button backToMenu;

    @FXML
    void onBackToMenu(ActionEvent event) {
    }

    @FXML
    void initialize() {
        assert backToMenu != null : "fx:id=\"backToMenu\" was not injected: check your FXML file 'endScrenn.fxml'.";
    }

}