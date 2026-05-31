package projekt.frontEnd;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.extern.log4j.Log4j2;

import static projekt.frontEnd.Game.HEIGHT;

@Log4j2
public class Level2 implements Level, PlatformListener {
    private Game game;

    private Scene[] ledges = new Scene[4];
    private Scene extraPlatform;
    private Spike[] spikes;
    private Scene finalDoor;

    private boolean platform2Moved = false;
    private boolean extraPlatformCreated = false;

    private boolean platform3Hidden = false;
    private boolean platform3TrapFinished = false;

    private Entity pushCube;
    private boolean pushCubeSpawned = false;

    @Override
    public void init(Game game) {
        this.game = game;

        ledges[0] = new Scene(40, 260, 220, 20);
        ledges[1] = new Scene(460, 460, 220, 20);
        ledges[2] = new Scene(840, 800, 220, 20);
        ledges[3] = new Scene(1400, HEIGHT - 120, 490, 20);

        int spikeSize = 40;
        double floorY = HEIGHT - 40;

        int count = (int) (Game.WIDTH / spikeSize);
        spikes = new Spike[count];

        for (int i = 0; i < count; i++) {
            spikes[i] = new Spike(i * spikeSize, floorY - spikeSize, spikeSize);
        }

        for (Scene s : ledges) {
            s.setPlatformListener(this);
            s.setVisible(true);
        }

        finalDoor = new Scene(Game.WIDTH - 80, ledges[3].getPlatformY() - 80, 40, 80, true, game);
    }

    @Override
    public void update(double delta) {

        Entity player = game.getPlayer();

        for (Scene s : ledges) {
            s.onCollision(player);
        }

        if (extraPlatform != null) {
            extraPlatform.onCollision(player);
        }

        finalDoor.onCollision(player);

        if (pushCube != null) {
            pushCube.setEntityVelocityY(0);
            pushCube.setEntityY(ledges[3].getPlatformY() - pushCube.getEntityHeight());

            double speed = 850;
            pushCube.setEntityX(pushCube.getEntityX() - speed * delta);

            if (pushCube.intersects(player.getBoundingBox())) {
                double newPlayerX = pushCube.getEntityX() - player.getEntityWidth() - 1;

                player.setEntityX(newPlayerX);

                player.setOnGround(false);
                player.setEntityVelocityY(200);
            }


            if (pushCube.getEntityX() + pushCube.getEntityWidth() < ledges[3].getPlatformX()) {
                pushCube = null;
            }

        }

        for (Spike s : spikes) {
            if (s.intersects(player.getBoundingBox())) {
                game.restartLevel();
                return;
            }
        }
    }

    @Override
    public void onPlatformLanded(Scene platform) {
        if (platform == ledges[1] && !platform2Moved) {
            platform2Moved = true;

            Scene p1 = ledges[0];

            double spikeTopY = HEIGHT - 40 - 40;
            double newY = spikeTopY - 20;

            Scene moved = new Scene(p1.getPlatformX(), newY, 220, 20);

            moved.setPlatformListener(this);
            moved.setVisible(true);

            ledges[1] = moved;
            return;
        }

        if (platform == ledges[1] && platform2Moved && !extraPlatformCreated) {
            extraPlatformCreated = true;

            extraPlatform = new Scene(platform.getPlatformX() + 400, platform.getPlatformY() - 160, 220, 20);

            extraPlatform.setPlatformListener(this);
            extraPlatform.setVisible(true);
            return;
        }

        if (platform == ledges[2] && extraPlatformCreated && !platform3TrapFinished) {
            platform3Hidden = true;
            ledges[2].setVisible(false);
            return;
        }

        if (platform == extraPlatform && platform3Hidden && !platform3TrapFinished) {
            platform3Hidden = false;
            platform3TrapFinished = true;
            ledges[2].setVisible(true);
            return;
        }

        if (platform == ledges[3] && !pushCubeSpawned) {

            pushCubeSpawned = true;

            pushCube = new Entity(finalDoor.getPlatformX() - 60, ledges[3].getPlatformY() - 40, 60, 60
            );

            pushCube.setOnGround(true);
            pushCube.setEntityVelocityY(0);
        }
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

        if (extraPlatform != null) {
            extraPlatform.draw(gc, Color.GREEN);
        }

        for (Spike s : spikes) {
            s.draw(gc);
        }

        if (pushCube != null) {
            gc.setFill(Color.GREEN);
            gc.fillRect(
                    pushCube.getEntityX(),
                    pushCube.getEntityY(),
                    pushCube.getEntityWidth(),
                    pushCube.getEntityHeight()
            );
        }

        finalDoor.draw(gc, Color.RED);
    }

    @Override
    public double getStartX() {
        return ledges[0].getPlatformX() + 40;
    }

    @Override
    public double getStartY() {
        return ledges[0].getPlatformY() - 96;
    }
}
