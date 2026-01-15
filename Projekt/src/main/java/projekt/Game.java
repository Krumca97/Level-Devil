package projekt;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.HashSet;
import java.util.Set;
import projekt.history.GameRecord;


public class Game extends Application implements GameEventListener {
    public static final double WIDTH = 1920;
    public static final double HEIGHT = 1080;

    private static Game instance;
    public static boolean userChecked = false;

    private Stage primaryStage;
    private AnimationTimer timer;
    private GraphicsContext gc;

    private boolean left;
    private boolean right;
    private boolean jump;

    private Entity player;
    private long levelStartTime;

    private Level currentLevel;
    public static int selectedLevel = 1;
    public static int playerBonus = 0;
    public static Set<Integer> completedLevels = new HashSet<>();


    @Override
    public void start(Stage stage) {
        instance = this;
        this.primaryStage = stage;

        SoundManager.playBackgroundMusic();

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        javafx.scene.Scene scene = new javafx.scene.Scene(new Group(canvas), WIDTH, HEIGHT);

        player = new Entity(120, HEIGHT - 220, 64, 96);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.A || e.getCode() == KeyCode.LEFT) left = true;
            if (e.getCode() == KeyCode.D || e.getCode() == KeyCode.RIGHT) right = true;
            if (e.getCode() == KeyCode.SPACE) jump = true;
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.A || e.getCode() == KeyCode.LEFT) left = false;
            if (e.getCode() == KeyCode.D || e.getCode() == KeyCode.RIGHT) right = false;
            if (e.getCode() == KeyCode.SPACE) jump = false;
            if (e.getCode() == KeyCode.ESCAPE) {exitToLevelSelect();}
        });

        stage.setScene(scene);
        stage.setTitle("Level Devil");
        stage.show();

        stage.setOnCloseRequest(event -> {SaveManager.saveCompletedLevels(Game.completedLevels);});
        loadLevel(selectedLevel);

        timer = new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last < 0) {
                    last = now;
                    return;
                }

                double delta = (now - last) / 1_000_000_000.0;
                last = now;

                update(delta);
                render();
            }
        };
        timer.start();
    }

    private void update(double delta) {
        player.input(left, right, jump);
        player.update(delta);
        player.keepInWorld(0, WIDTH, HEIGHT);

        if (currentLevel != null) {
            currentLevel.update(delta);
        }
    }

    private void render() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        if (currentLevel != null) {
            currentLevel.render(gc);
        }

        player.draw(gc);
    }

    private void loadLevel(int levelNumber) {
        if (levelNumber == 1) {
            currentLevel = new Level1();
        } else if (levelNumber == 2) {
            currentLevel = new Level2();
        }
        if (currentLevel != null) {
            currentLevel.init(this);
        }

        resetPlayer();
    }

    public void restartLevel() {
        loadLevel(selectedLevel);
    }

    public void setLevel(int level) {
        this.selectedLevel = level;
        loadLevel(level);
    }

    private void resetPlayer() {
        if (currentLevel != null) {
            player.setEntityX(currentLevel.getStartX());
            player.setEntityY(currentLevel.getStartY());
        }

        player.setEntityVelocityX(0);
        player.setEntityVelocityY(0);
        player.setOnGround(false);
        levelStartTime = System.currentTimeMillis();
    }

    public Entity getPlayer() {
        return player;
    }

    @Override
    public void onLevelFinished() {

        Game.completedLevels.add(Game.selectedLevel);
        SaveManager.saveCompletedLevels(Game.completedLevels);
        timer.stop();

        javafx.application.Platform.runLater(() -> {

            if (primaryStage != null) {
                primaryStage.close();
            }

            try {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/projekt/level_select.fxml"));

                javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());

                Stage levelSelectStage = new Stage();
                levelSelectStage.setTitle("Select Level");
                levelSelectStage.setScene(scene);
                levelSelectStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        long timeSpent = System.currentTimeMillis() - levelStartTime;

        GameRecord record = new GameRecord(
                MenuController.getUserName(),
                Game.selectedLevel,
                timeSpent,
                System.currentTimeMillis()
        );

        projekt.history.HistoryManager.saveRecord(record);
    }

    private void exitToLevelSelect() {
        if (timer != null) {
            timer.stop();
        }

        javafx.application.Platform.runLater(() -> {
            try {
                if (primaryStage != null) {
                    primaryStage.close();
                }

                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/projekt/level_select.fxml"));

                javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());

                Stage stage = new Stage();
                stage.setTitle("Select Level");
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
