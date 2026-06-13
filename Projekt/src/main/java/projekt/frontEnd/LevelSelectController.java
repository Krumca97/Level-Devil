package projekt.frontEnd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LevelSelectController {
    @FXML private Button level1Button;
    @FXML private Button level2Button;
    @FXML private Button level3Button;

    @FXML
    private void initialize() {
        if (Game.completedLevels.contains(1)) {
            level1Button.setStyle("-fx-background-color: #2ecc71;");
        }

        if (Game.completedLevels.contains(2)) {
            level2Button.setStyle("-fx-background-color: #2ecc71;");
        }

        if (Game.completedLevels.contains(3)) {
            level3Button.setStyle("-fx-background-color: #2ecc71;");
        }
    }

    private void startLevel(int level, ActionEvent event) {
        try {
            Game.selectedLevel = level;

            Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            oldStage.close();

            Stage stage = new Stage();
            new Game().start(stage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBack(ActionEvent event) {
        try {
            Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            oldStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/projekt/menu.fxml"));

            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Main Menu");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onLevel1(ActionEvent event) {
        startLevel(1, event);
    }

    @FXML
    private void onLevel2(ActionEvent event) {
        startLevel(2, event);
    }

    @FXML
    private void onLevel3(ActionEvent event) {
        startLevel(3, event);
    }
}
