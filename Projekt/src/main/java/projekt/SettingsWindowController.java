package projekt;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class SettingsWindowController {

    @FXML
    private TextField inputDifficulty;

    @FXML
    private TextField inputUser;

    @FXML
    void onSave() {
        Settings s = new Settings();

        s.set("difficulty", inputDifficulty.getText());
        s.set("user", inputUser.getText());

        try {
            s.save();
            System.out.println("Settings saved");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Saved");
            alert.setContentText("Settings saved");
            alert.showAndWait();

        } catch (SettingsException e) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Saving problem");
            alert.getDialogPane().setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void onLoad() {
        Settings s = new Settings();
        try {
            s.load();
            inputDifficulty.setText(s.get("difficulty", ""));
            inputUser.setText(s.get("user", ""));
        } catch (SettingsException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Loading problem");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
