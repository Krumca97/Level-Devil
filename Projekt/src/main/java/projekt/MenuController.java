package projekt;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button historyButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button startGame;

    @FXML
    void onHistory(ActionEvent event) {
        System.out.println("History");
    }

    @FXML
    void onSettings(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/projekt/settings.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Settings");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onStartGame(ActionEvent event) {
        ((Button) event.getSource()).getScene().getWindow().hide();
        Game game = new Game();
        Stage stage = new Stage();
        game.start(stage);
    }
    private String askUserName() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Prihlaseni");
        dialog.setHeaderText("Prosim vlozte sve jmeno(jako na strankach)");
        dialog.setContentText("Jmeno:");

        return dialog.showAndWait().orElse("").trim();
    }

    private void checkTeacher() {

        String userName = askUserName();

        if (userName.isEmpty()) {
            System.out.println("Uzivatel nezadal jmeno.");
            return;
        }

        Set<String> teachers = TeacherChecker.loadTeachersFromKatedra();

        if (teachers.isEmpty()) {
            System.out.println("Jsme offline nestahnu jmena ucitelu.");
            Game.playerBonus = 0;
            return;
        }

        if (TeacherChecker.isTeacher(userName, teachers)) {
            System.out.println("Uzivatel je clen fakulty");
            System.out.println("Uzivatel ziskava 100 bodu");
            Game.playerBonus = 100;
        } else {
            System.out.println("Uzivatel neni clen fakulty");
            System.out.println("Uzivatel dostava trest 10 bodu");
            Game.playerBonus = -10;
        }
    }

    @FXML
    void initialize() {
        assert historyButton != null : "fx:id=\"historyButton\" was not injected.";
        assert settingsButton != null : "fx:id=\"settingsButton\" was not injected.";
        assert startGame != null : "fx:id=\"startGame\" was not injected.";

        checkTeacher();
    }

}
