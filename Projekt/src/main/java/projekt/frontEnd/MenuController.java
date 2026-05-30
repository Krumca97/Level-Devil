package projekt.frontEnd;

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
    private static String userName;

    public static String getUserName() {
        return userName != null ? userName : "Unknown";
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private Button historyButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button startGame;

    @FXML
    void onHistory(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/projekt/history.fxml"));

            Stage stage = new Stage();
            stage.setTitle("History");
            stage.setScene(new javafx.scene.Scene(loader.load()));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
            ((Button) event.getSource()).getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/projekt/level_select.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Select Level");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String askUserName() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Prihlaseni");
        dialog.setHeaderText("Prosim vlozte sve jmeno (jako na strankach)");
        dialog.setContentText("Jmeno:");

        return dialog.showAndWait().orElse("").trim();
    }

    private void checkTeacher() {

        String name = askUserName();
        MenuController.userName = name;

        if (name.isEmpty()) {
            System.out.println("Uzivatel nezadal jmeno.");
            return;
        }

        Set<String> teachers = TeacherChecker.loadTeachersFromKatedra();

        if (teachers.isEmpty()) {
            System.out.println("Jsme offline – nestahnu jmena ucitelu.");
            Game.playerBonus = 0;
            return;
        }

        if (TeacherChecker.isTeacher(name, teachers)) {
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

        if (!Game.userChecked) {
            Game.userChecked = true;
            javafx.application.Platform.runLater(this::checkTeacher);
        }
    }
}
