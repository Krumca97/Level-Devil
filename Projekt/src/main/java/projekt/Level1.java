package projekt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static projekt.Game.HEIGHT;

public class Level1 implements Level, PlatformListener {
    private Game game;

    private Scene[] ledges = new Scene[4];

    private Entity[] bulletsP1 = new Entity[5];
    private Entity[] bulletsP2 = new Entity[5];
    private Entity[] bulletsP3 = new Entity[10];

    private Scene finalDoor;

    private int levelPhase = 0;
    private double shootTimer = 0;
    private int shotsFired = 0;

    private boolean firstPlatformTriggered = false;
    private boolean secondPlatformTriggered = false;
    private boolean thirdPlatformTriggered = false;
    private boolean wasFalling = false;

    private boolean lastPlatformUsedOnce = false;
    private boolean lastPlatformHidden = false;

    @Override
    public void init(Game game) {
        this.game = game;

        ledges[0] = new Scene(320, HEIGHT - 200, 220, 20);
        ledges[1] = new Scene(640, HEIGHT - 350, 220, 20);
        ledges[2] = new Scene(960, HEIGHT - 500, 220, 20);
        ledges[3] = new Scene(1280, HEIGHT - 650, 660, 20);

        ledges[0].setId(1);
        ledges[1].setId(2);
        ledges[2].setId(3);
        ledges[3].setId(4);

        for (Scene s : ledges) {
            s.setPlatformListener(this);
            s.setVisible(true);
        }

        initBullets(bulletsP1);
        initBullets(bulletsP2);
        initBullets(bulletsP3);

        finalDoor = new Scene(Game.WIDTH - 80, HEIGHT - 730, 40, 80, true, game);
    }

    @Override
    public void update(double delta) {

        Entity player = game.getPlayer();

        double floorY = HEIGHT - 40;
        boolean onFloor = player.getEntityY() + player.getEntityHeight() >= floorY;

        if (onFloor && wasFalling) {

            player.setEntityY(floorY - player.getEntityHeight());
            player.setEntityVelocityY(0);
            player.setOnGround(true);

            if (lastPlatformHidden) {
                lastPlatformHidden = false;
                ledges[3].setVisible(true);
                resetTrapsOnly();
            }
        }

        wasFalling = player.getEntityVelocityY() > 0;

        for (Scene s : ledges) {
            s.onCollision(player);
        }

        Scene p2 = ledges[1];
        double p2CenterX = p2.getPlatformX() + p2.getPlatformWidth() / 2;
        double playerCenterX = player.getEntityX() + player.getEntityWidth() / 2;
        double playerBottomY = player.getEntityY() + player.getEntityHeight();

        if (!secondPlatformTriggered &&
                levelPhase == 2 &&
                player.getEntityVelocityY() == 0 &&
                Math.abs(playerCenterX - p2CenterX) < 20 &&
                Math.abs(playerBottomY - p2.getPlatformY()) < 10) {

            secondPlatformTriggered = true;
            levelPhase = 3;
            shootTimer = 0;
            shotsFired = 0;
        }

        if (levelPhase == 1) {
            shootTimer += delta;
            if (shootTimer >= 0.15 && shotsFired < bulletsP1.length) {
                shootTimer = 0;
                Entity b = bulletsP1[shotsFired];
                b.setActive(true);
                b.setEntityX(0);
                b.setEntityY(ledges[0].getPlatformY() - 50);
                b.setEntityVelocityX(1400 + shotsFired * 200);
                b.setEntityVelocityY(0);
                shotsFired++;
            }
            if (shotsFired >= bulletsP1.length) levelPhase = 2;
        }

        if (levelPhase == 3) {
            shootTimer += delta;
            if (shootTimer >= 0.12 && shotsFired < bulletsP2.length) {
                shootTimer = 0;
                Entity b = bulletsP2[shotsFired];
                b.setActive(true);
                b.setEntityX(p2CenterX - b.getEntityWidth() / 2);
                b.setEntityY(HEIGHT - 40);
                b.setEntityVelocityX(0);
                b.setEntityVelocityY(-1600 - shotsFired * 250);
                shotsFired++;
            }
            if (shotsFired >= bulletsP2.length) levelPhase = 4;
        }

        if (levelPhase == 5) {
            shootTimer += delta;
            if (shootTimer >= 0.12 && shotsFired < bulletsP3.length) {
                shootTimer = 0;
                Entity b = bulletsP3[shotsFired];
                Scene p3 = ledges[2];
                double cx = p3.getPlatformX() + p3.getPlatformWidth() / 2;

                b.setActive(true);
                if (shotsFired < 5) {
                    b.setEntityX(0);
                    b.setEntityY(p3.getPlatformY() - 50);
                    b.setEntityVelocityX(1800);
                    b.setEntityVelocityY(0);
                } else {
                    b.setEntityX(cx - b.getEntityWidth() / 2);
                    b.setEntityY(HEIGHT - 40);
                    b.setEntityVelocityX(0);
                    b.setEntityVelocityY(-1800);
                }
                shotsFired++;
            }
        }

        finalDoor.onCollision(player);

        updateBullets(bulletsP1, delta, player);
        updateBullets(bulletsP2, delta, player);
        updateBullets(bulletsP3, delta, player);
    }

    @Override
    public void render(GraphicsContext gc) {

        gc.setFill(Color.rgb(230, 255, 230));
        gc.fillRect(0, 0, Game.WIDTH, HEIGHT);

        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, Game.WIDTH, 40);
        gc.fillRect(0, HEIGHT - 40, Game.WIDTH, 40);
        gc.fillRect(0, 0, 40, HEIGHT);
        gc.fillRect(Game.WIDTH - 40, 0, 40, HEIGHT);

        for (Scene s : ledges) {
            s.draw(gc, Color.GREEN);
        }

        drawBullets(gc, bulletsP1);
        drawBullets(gc, bulletsP2);
        drawBullets(gc, bulletsP3);

        finalDoor.draw(gc, Color.RED);
    }

    @Override
    public void onPlatformLanded(Scene platform) {

        if (platform.getId() == 1 && !firstPlatformTriggered) {
            firstPlatformTriggered = true;
            levelPhase = 1;
            shootTimer = 0;
            shotsFired = 0;
        }

        if (platform.getId() == 3 && !thirdPlatformTriggered) {
            thirdPlatformTriggered = true;
            levelPhase = 5;
            shootTimer = 0;
            shotsFired = 0;
        }

        if (platform.getId() == 4 && !lastPlatformUsedOnce) {
            lastPlatformUsedOnce = true;
            lastPlatformHidden = true;
            platform.setVisible(false);
        }
    }

    private void resetTrapsOnly() {

        levelPhase = 0;
        shootTimer = 0;
        shotsFired = 0;

        firstPlatformTriggered = false;
        secondPlatformTriggered = false;
        thirdPlatformTriggered = false;

        resetBullets(bulletsP1);
        resetBullets(bulletsP2);
        resetBullets(bulletsP3);

        Entity player = game.getPlayer();
        player.setEntityVelocityX(0);
        player.setEntityVelocityY(0);
        player.setOnGround(true);
    }

    private void initBullets(Entity[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new Entity(-100, -100, 50, 50);
            arr[i].setIsBullet(true);
            arr[i].setActive(false);
        }
    }

    private void resetBullets(Entity[] arr) {
        for (Entity b : arr) {
            b.setActive(false);
            b.setEntityX(-100);
            b.setEntityY(-100);
            b.setEntityVelocityX(0);
            b.setEntityVelocityY(0);
        }
    }

    private void updateBullets(Entity[] arr, double delta, Entity player) {
        for (Entity b : arr) {

            b.update(delta);

            if (b.isActive() && b.intersects(player.getBoundingBox())) {
                game.restartLevel();
                return;
            }

            b.keepInWorld(0, Game.WIDTH, HEIGHT);
        }
    }


    private void drawBullets(GraphicsContext gc, Entity[] bullets) {
        for (Entity b : bullets) {
            if (b.isActive()) {
                b.draw(gc);
            }
        }
    }

    @Override
    public double getStartX() {
        return 120;
    }

    @Override
    public double getStartY() {
        return HEIGHT-220;
    }
}
