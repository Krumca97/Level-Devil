package projekt;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Scene implements Collisionable {

    // pozice a velikost
    private final double platformX;
    private final double platformY;
    private final double platformWidth;
    private final double platformHeight;

    // LEVEL DEVIL
    private int id = -1;
    private boolean isFinal = false;
    private GameEventListener listener;
    private boolean wasPlayerOnThis = false;

    public Scene(double platformX, double platformY, double platformWidth, double platformHeight) {
        this.platformX = platformX;
        this.platformY = platformY;
        this.platformWidth = platformWidth;
        this.platformHeight = platformHeight;
    }

    public Scene(double x, double y, double width, double height, boolean isFinal, GameEventListener listener) {
        this(x, y, width, height);
        this.isFinal = isFinal;
        this.listener = listener;
    }

    // ===== ID PLATFORMY =====
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    // ===== DRAW =====
    public void draw(GraphicsContext gc, Color color) {
        gc.setFill(color);
        gc.fillRect(platformX, platformY, platformWidth, platformHeight);
    }

    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(platformX, platformY, platformWidth, platformHeight);
    }

    @Override
    public boolean intersects(Rectangle2D other) {
        return getBoundingBox().intersects(other);
    }

    @Override
    public void onCollision(Collisionable other) {
        if (!(other instanceof Entity player)) return;

        Rectangle2D playerBox = player.getBoundingBox();
        Rectangle2D platformBox = getBoundingBox();

        if (!playerBox.intersects(platformBox)) {
            // hráč už na platformě není
            wasPlayerOnThis = false;
            return;
        }

        // === DOSKOK SHORA (JEN JEDNOU) ===
        boolean landingFromAbove =
                player.getEntityVelocityY() >= 0 &&
                        playerBox.getMaxY() <= platformBox.getMinY() + 10;

        if (!isFinal && landingFromAbove) {

            player.setEntityY(platformY - player.getEntityHeight());
            player.setEntityVelocityY(0);
            player.setOnGround(true);

            // 🔥 EVENT JEN PŘI PRVNÍM DOTYKU
            if (!wasPlayerOnThis) {
                wasPlayerOnThis = true;
                Game.onPlatformLanded(this);
            }
        }

        // === FINAL ===
        if (isFinal && listener != null) {
            listener.onLevelFinished();
        }
    }


    // ===== GETTERS =====
    public double getPlatformX() {
        return platformX;
    }

    public double getPlatformY() {
        return platformY;
    }

    public double getPlatformWidth() {
        return platformWidth;
    }

    public double getPlatformHeight() {
        return platformHeight;
    }
}
