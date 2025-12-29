package projekt;

import java.util.Objects;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Entity implements Collisionable {

    // pozice a velikost
    private double entityX;
    private double entityY;
    private double entityVelocityX;
    private double entityVelocityY;
    private double entityWidth;
    private double entityHeight;

    private boolean isBullet = false;
    private boolean onGround = false;
    private boolean active = true;

    // bullet animace
    private Image[] bulletFrames;
    private int bulletFrameIndex = 0;
    private double bulletFrameTimer = 0.0;

    // pohyb
    private static final double MOVE_SPEED = 200;
    private static final double JUMP_SPEED = -450;
    private static final double GRAVITY = 600;

    // input
    private boolean left;
    private boolean right;
    private boolean jump;

    public Entity(double x, double y, double w, double h) {
        this.entityX = x;
        this.entityY = y;
        this.entityWidth = w;
        this.entityHeight = h;
    }

    // ===== INPUT =====
    public void input(boolean left, boolean right, boolean jump) {
        this.left = left;
        this.right = right;
        this.jump = jump;
    }

    // ===== UPDATE =====
    public void update(double deltaTime) {

        if (!active) return;

        if (isBullet) {
            entityX += entityVelocityX * deltaTime;
            entityY += entityVelocityY * deltaTime;

            // animace
            if (bulletFrames != null) {
                bulletFrameTimer += deltaTime;
                double bulletFrameDuration = 0.10;
                while (bulletFrameTimer >= bulletFrameDuration) {
                    bulletFrameTimer -= bulletFrameDuration;
                    bulletFrameIndex = (bulletFrameIndex + 1) % bulletFrames.length;
                }
            }
            return;
        }

        entityVelocityX = 0;

        if (left) entityVelocityX -= MOVE_SPEED;
        if (right) entityVelocityX += MOVE_SPEED;

        if (jump && onGround) {
            entityVelocityY = JUMP_SPEED;
            onGround = false;
        }

        entityVelocityY += GRAVITY * deltaTime;

        entityX += entityVelocityX * deltaTime;
        entityY += entityVelocityY * deltaTime;
    }

    // ===== DRAW =====
    public void draw(GraphicsContext gc) {

        if (!active) return;

        if (isBullet && bulletFrames != null) {
            gc.drawImage(
                    bulletFrames[bulletFrameIndex],
                    entityX, entityY,
                    entityWidth, entityHeight
            );
        } else {
            gc.setFill(Color.RED);
            gc.fillRect(entityX, entityY, entityWidth, entityHeight);
        }

        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        gc.strokeRect(entityX, entityY, entityWidth, entityHeight);
    }

    // ===== WORLD BOUNDS =====
    public void keepInWorld(double minX, double maxX, double maxY) {

        if (!active) return;

        if (isBullet) {
            // 🔥 BULLET = JEDNORÁZOVÝ
            if (entityX > maxX || entityX + entityWidth < minX) {
                active = false; // zmizí navždy
            }
            return;
        }

        // hráč
        if (entityX < minX) entityX = minX;
        if (entityX + entityWidth > maxX) entityX = maxX - entityWidth;

        if (entityY + entityHeight > maxY) {
            entityY = maxY - entityHeight;
            entityVelocityY = 0;
            onGround = true;
        }
    }

    // ===== BULLET MODE =====
    public void setIsBullet(boolean bullet) {
        this.isBullet = bullet;

        if (bullet) {
            bulletFrames = new Image[]{
                    new Image(Objects.requireNonNull(getClass().getResource("/projekt/bullet1.png")).toExternalForm()),
                    new Image(Objects.requireNonNull(getClass().getResource("/projekt/bullet2.png")).toExternalForm()),
                    new Image(Objects.requireNonNull(getClass().getResource("/projekt/bullet3.png")).toExternalForm()),
                    new Image(Objects.requireNonNull(getClass().getResource("/projekt/bullet4.png")).toExternalForm())
            };
            entityWidth = 50;
            entityHeight = 50;
        } else {
            bulletFrames = null;
            bulletFrameIndex = 0;
            bulletFrameTimer = 0;
        }
    }

    // ===== GET / SET =====
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getEntityX() {
        return entityX;
    }

    public void setEntityX(double x) {
        this.entityX = x;
    }

    public double getEntityY() {
        return entityY;
    }

    public void setEntityY(double y) {
        this.entityY = y;
    }

    public double getEntityWidth() {
        return entityWidth;
    }

    public double getEntityHeight() {
        return entityHeight;
    }

    public double getEntityVelocityY() {
        return entityVelocityY;
    }

    public void setEntityVelocityX(double v) {
        this.entityVelocityX = v;
    }

    public void setEntityVelocityY(double v) {
        this.entityVelocityY = v;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    // ===== COLLISION =====
    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(entityX, entityY, entityWidth, entityHeight);
    }

    @Override
    public boolean intersects(Rectangle2D other) {
        return active && getBoundingBox().intersects(other);
    }

    @Override
    public void onCollision(Collisionable other) {
        // řeší Game / Scene
    }
}
