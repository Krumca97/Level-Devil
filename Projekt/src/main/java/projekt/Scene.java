package projekt;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Scene implements Collisionable {
    private final double platformX;
    private final double platformY;
    private final double platformWidth;
    private final double platformHeight;

    private int id = -1;
    private boolean isFinal = false;
    private GameEventListener listener;
    private boolean wasPlayerOnThis = false;
    private boolean visible = true;
    private PlatformListener platformListener;


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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void draw(GraphicsContext gc, Color color) {
        if (!visible) return;
        gc.setFill(color);
        gc.fillRect(platformX, platformY, platformWidth, platformHeight);
    }


    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(platformX, platformY, platformWidth, platformHeight);
    }

    @Override
    public boolean intersects(Rectangle2D other) {
        if (!visible) {
            return false;
        }

        return getBoundingBox().intersects(other);
    }

    @Override
    public void onCollision(Collisionable other) {
        if (!visible) {
            return;
        }
        if (!(other instanceof Entity player)) {
            return;
        }

        Rectangle2D playerBox = player.getBoundingBox();
        Rectangle2D platformBox = getBoundingBox();

        if (!playerBox.intersects(platformBox)) {
            wasPlayerOnThis = false;
            return;
        }

        boolean landingFromAbove = player.getEntityVelocityY() >= 0 && playerBox.getMaxY() <= platformBox.getMinY() + 10;

        if (!isFinal && landingFromAbove) {
            player.setEntityY(platformY - player.getEntityHeight());
            player.setEntityVelocityY(0);
            player.setOnGround(true);

            if (!wasPlayerOnThis) {
                wasPlayerOnThis = true;
                if (platformListener != null) {
                    platformListener.onPlatformLanded(this);
                }
            }
        }

        if (isFinal && listener != null) {
            listener.onLevelFinished();
        }
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public double getPlatformX() {
        return platformX;
    }

    public double getPlatformY() {
        return platformY;
    }

    public double getPlatformWidth() {
        return platformWidth;
    }

    public void setPlatformListener(PlatformListener listener) {
        this.platformListener = listener;
    }
}
