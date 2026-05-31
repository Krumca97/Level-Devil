package projekt.frontEnd;

import java.util.Objects;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import projekt.frontEnd.Collisionable;

@Getter
@Setter
@ToString
public class Entity implements Collisionable {
    private double entityX;
    private double entityY;
    private double entityVelocityX;
    private double entityVelocityY;
    private double entityWidth;
    private double entityHeight;

    private boolean bullet = false;
    private boolean onGround = false;
    private boolean active = true;

    private Image[] bulletFrames;
    private int bulletFrameIndex = 0;
    private double bulletFrameTimer = 0.0;

    private Image playerGif;

    private static final double MOVE_SPEED = 200;
    private static final double JUMP_SPEED = -450;
    private static final double GRAVITY = 600;

    private boolean left;
    private boolean right;
    private boolean jump;

    public Entity(double x, double y, double w, double h) {
        this.entityX = x;
        this.entityY = y;
        this.entityWidth = w;
        this.entityHeight = h;
        loadPlayerGif();
    }

    private void loadPlayerGif() {
        try {playerGif = new Image(
                    Objects.requireNonNull(getClass().getResource("/projekt/player_run.gif")).toExternalForm());
        } catch (Exception e) {
            playerGif = null;
        }
    }

    public void input(boolean left, boolean right, boolean jump) {
        this.left = left;
        this.right = right;
        this.jump = jump;
    }

    public void update(double deltaTime) {
        if (!active) return;

        if (bullet) {
            entityX += entityVelocityX * deltaTime;
            entityY += entityVelocityY * deltaTime;

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

        if (left) {
            entityVelocityX -= MOVE_SPEED;
        }
        if (right) {
            entityVelocityX += MOVE_SPEED;
        }

        if (jump && onGround) {
            entityVelocityY = JUMP_SPEED;
            onGround = false;
        }

        entityVelocityY += GRAVITY * deltaTime;

        entityX += entityVelocityX * deltaTime;
        entityY += entityVelocityY * deltaTime;
    }

    public void draw(GraphicsContext gc) {

        if (!active) {
            return;
        }

        if (bullet && bulletFrames != null) {
            gc.drawImage(bulletFrames[bulletFrameIndex], entityX, entityY, entityWidth, entityHeight);
        } else if (playerGif != null) {
            gc.drawImage(playerGif, entityX, entityY, entityWidth, entityHeight);
        } else {
            gc.setFill(Color.RED);
            gc.fillRect(entityX, entityY, entityWidth, entityHeight);
        }
        //debug bounding box
//        gc.setStroke(Color.BLUE);
//        gc.setLineWidth(2);
//        gc.strokeRect(entityX, entityY, entityWidth, entityHeight);
    }

    public void keepInWorld(double minX, double maxX, double maxY) {

        if (!active) {
            return;
        }

        if (bullet) {
            if (entityX > maxX || entityX + entityWidth < minX || entityY > maxY || entityY + entityHeight < 0) {
                active = false;
            }
            return;
        }

        if (entityX < minX) {
            entityX = minX;
            entityVelocityX = 0;
        }

        if (entityX + entityWidth > maxX) {
            entityX = maxX - entityWidth;
            entityVelocityX = 0;
        }

        if (entityY < 40) {
            entityY = 40;
            if (entityVelocityY < 0) {
                entityVelocityY = 0;
            }
        }

        if (entityY + entityHeight > maxY - 40) {
            entityY = maxY - 40 - entityHeight;
            entityVelocityY = 0;
            onGround = true;
        }
    }


    public void setBullet(boolean bullet) {
        this.bullet = bullet;

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
        //pass
    }
}
