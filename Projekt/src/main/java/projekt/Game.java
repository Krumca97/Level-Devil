package projekt;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Game extends Application implements GameEventListener {

    private static Game instance;
    public static int playerBonus = 0;


    private static final double WIDTH = 1920;
    private static final double HEIGHT = 1080;

    private boolean left;
    private boolean right;
    private boolean jump;

    private Scene[] ledges = new Scene[4];
    private Entity[] bullets = new Entity[5];

    private AnimationTimer timer;
    private Stage primaryStage;

    // ===== LEVEL DEVIL STATE =====
    private int levelPhase = 0;
    private double shootTimer = 0;
    private int shotsFired = 0;

    private boolean firstPlatformTriggered = false;
    private boolean secondPlatformTriggered = false;

    @Override
    public void start(Stage stage) {
        instance = this;
        this.primaryStage = stage;

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Renderer renderer = new Renderer();
        javafx.scene.Scene scene = new javafx.scene.Scene(new Group(canvas), WIDTH, HEIGHT);

        Entity player = new Entity(120, HEIGHT - 220, 32, 48);

        Scene ground = new Scene(0, HEIGHT - 40, WIDTH, 48);
        FinalScene doorFinal = new FinalScene(1890, HEIGHT - 700, 30, 50, this);

        // ===== PLATFORMS =====
        ledges[0] = new Scene(320, HEIGHT - 200, 220, 20); // P1
        ledges[1] = new Scene(640, HEIGHT - 350, 220, 20); // P2
        ledges[2] = new Scene(960, HEIGHT - 500, 220, 20);
        ledges[3] = new Scene(1280, HEIGHT - 650, 660, 20);

        ledges[0].setId(1);
        ledges[1].setId(2);
        ledges[2].setId(3);
        ledges[3].setId(4);

        // ===== BULLETS =====
        for (int i = 0; i < bullets.length; i++) {
            bullets[i] = new Entity(-100, -100, 50, 50);
            bullets[i].setIsBullet(true);
            bullets[i].setActive(false);
        }

        // ===== INPUT =====
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.A || e.getCode() == KeyCode.LEFT) left = true;
            if (e.getCode() == KeyCode.D || e.getCode() == KeyCode.RIGHT) right = true;
            if (e.getCode() == KeyCode.SPACE) jump = true;
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.A || e.getCode() == KeyCode.LEFT) left = false;
            if (e.getCode() == KeyCode.D || e.getCode() == KeyCode.RIGHT) right = false;
            if (e.getCode() == KeyCode.SPACE) jump = false;
        });

        stage.setScene(scene);
        stage.setTitle("Level Devil demo");
        stage.show();

        // ===== GAME LOOP =====
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

                player.input(left, right, jump);
                player.update(delta);

                ground.onCollision(player);
                for (Scene s : ledges) s.onCollision(player);
                doorFinal.onCollision(player);

                player.keepInWorld(0, WIDTH, HEIGHT);

                // ===== PLATFORM 2 CENTER CHECK =====
                Scene p2 = ledges[1];

                // střed X
                double p2CenterX = p2.getPlatformX() + p2.getPlatformWidth() / 2;
                double playerCenterX = player.getEntityX() + player.getEntityWidth() / 2;

                // Y pozice
                double playerBottomY = player.getEntityY() + player.getEntityHeight();
                double platformTopY = p2.getPlatformY();

                if (!secondPlatformTriggered &&
                        levelPhase == 2 &&


                        playerBottomY < platformTopY &&
                        playerBottomY > platformTopY - 5 &&

                        // 🔥 HRÁČ JE U STŘEDU
                        Math.abs(playerCenterX - p2CenterX) < 30
                ) {
                    secondPlatformTriggered = true;
                    levelPhase = 3;
                    shootTimer = 0;
                    shotsFired = 0;
                }


                // ===== PLATFORM 1 BURST (HORIZONTAL) =====
                if (levelPhase == 1) {
                    shootTimer += delta;

                    if (shootTimer >= 0.15 && shotsFired < bullets.length) {
                        shootTimer = 0;

                        Entity b = bullets[shotsFired];
                        b.setActive(true);
                        b.setEntityX(0);
                        b.setEntityY(ledges[0].getPlatformY() - 50);
                        b.setEntityVelocityX(1400 + shotsFired * 200);
                        b.setEntityVelocityY(0);

                        shotsFired++;
                    }

                    if (shotsFired >= bullets.length) {
                        levelPhase = 2;
                    }
                }

                // ===== PLATFORM 2 BURST (VERTICAL) =====
                if (levelPhase == 3) {
                    shootTimer += delta;

                    if (shootTimer >= 0.12 && shotsFired < bullets.length) {
                        shootTimer = 0;

                        Entity b = bullets[shotsFired];
                        b.setActive(true);
                        b.setEntityX(p2CenterX - b.getEntityWidth() / 2);
                        b.setEntityY(HEIGHT - 40);
                        b.setEntityVelocityX(0);
                        b.setEntityVelocityY(-900 - shotsFired * 150);

                        shotsFired++;
                    }

                    if (shotsFired >= bullets.length) {
                        levelPhase = 4;
                    }
                }

                // ===== BULLET UPDATE =====
                for (Entity b : bullets) {
                    b.update(delta);
                    b.keepInWorld(0, WIDTH, HEIGHT);

                    if (b.intersects(player.getBoundingBox())) {
                        restartLevel();
                        return;
                    }
                }

                // ===== RENDER =====
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, WIDTH, HEIGHT);

                renderer.clearEntities();
                renderer.addEntity(player);
                renderer.addEntity(ground);
                for (Scene s : ledges) renderer.addEntity(s);
                renderer.addEntity(doorFinal);
                for (Entity b : bullets) renderer.addEntity(b);

                renderer.render(gc);
            }
        };

        timer.start();
    }

    // ===== EVENT Z PLATFORMY 1 =====
    public static void onPlatformLanded(Scene platform) {
        if (instance != null) {
            instance.platformLanded(platform);
        }
    }

    private void platformLanded(Scene platform) {
        if (platform.getId() == 1 && !firstPlatformTriggered) {
            firstPlatformTriggered = true;
            levelPhase = 1;
            shootTimer = 0;
            shotsFired = 0;
        }
    }

    private void restartLevel() {
        timer.stop();
        levelPhase = 0;
        shootTimer = 0;
        shotsFired = 0;
        firstPlatformTriggered = false;
        secondPlatformTriggered = false;
        start(primaryStage);
    }

    @Override
    public void onLevelFinished() {
        timer.stop();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
