package projekt;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Objects;

public class Entity {
    //pozice a velikost entity
    private  double entityX;
    private double entityY;
    private double entityVelocityX;
    private double entityVelocityY;
    private double entityWidth;
    private double entityHeight;

    private boolean isBullet = false;

    private boolean onGround = false;

    //bullets
    private Image[] bulletFrames;
    private int bulletFrameIndex = 0;
    private double bulletFrameTimer = 0.0;

    //pohyb
    private static final double MOVE_SPEED = 200;
    private static final double JUMP_SPEED = -450;
    private static final double GRAVITY = 600;

    //tlacitka pro pohyb
    private boolean left;
    private boolean right;
    private boolean jump;

    public Entity(double entityX, double entityY, double entityWidth, double entityHeight) {
        this.entityX = entityX;
        this.entityY = entityY;
        this.entityWidth = entityWidth;
        this.entityHeight = entityHeight;
    }

    public void input(boolean left, boolean right, boolean jump) {
        this.left = left;
        this.right = right;
        this.jump = jump;
    }

    //Zmena pohybu
    public void update(double deltaTime) {
        if(isBullet){
            entityX += entityVelocityX * deltaTime;
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
        else {
            entityVelocityX = 0.0;
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
    }

    public void draw(GraphicsContext gc) {
        if (isBullet) {
            if (bulletFrames != null && bulletFrames.length > 0) {
                int idx = bulletFrameIndex % bulletFrames.length;
                gc.drawImage(bulletFrames[idx], entityX, entityY, entityWidth, entityHeight);
            }
        }
        else{
            gc.setFill(Color.RED);
            gc.fillRect(entityX, entityY, entityWidth, entityHeight);
        }
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        gc.strokeRect(entityX, entityY, entityWidth, entityHeight);
    }

    //zabraneni vystoupeni mimo okno
    public void keepInWorld(double minX, double maxX, double maxY) {
        if(isBullet) {
            if (entityX > maxX) {
                entityX = minX - entityWidth;
            }
            if (entityX + entityWidth < minX) {
                entityX =maxX;
            }
        }
        else {
            if (entityX < minX) {
                entityX = minX;
            }
            if (entityX + entityWidth > maxX) {
                entityX = maxX - entityWidth;
            }
            if (entityY + entityHeight > maxY) {
                entityY = maxY - entityHeight;
                entityVelocityY = 0;
                onGround = true;
            }
        }
    }

    //gettery a settery
    public double getEntityX(){
        return entityX;
    }

    public double getEntityY(){
        return entityY;
    }

    public double getEntityWidth(){
        return entityWidth;
    }

    public double getEntityHeight(){
        return entityHeight;
    }

    public double getEntityVelocityY(){
        return entityVelocityY;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void setEntityY(double entityY) {
        this.entityY = entityY;
    }

    public void setEntityVelocityX(double entityVelocityX) {
        this.entityVelocityX = entityVelocityX;
    }

    public void setEntityVelocityY(double entityVelocityY) {
        this.entityVelocityY = entityVelocityY;
    }
    public void setIsBullet(boolean is) {
        this.isBullet = is;
        if(isBullet) {
            bulletFrames = new Image[]{
                    new Image(Objects.requireNonNull(getClass().getResource("/projekt/bullet1.png")).toExternalForm()),
                    new Image(Objects.requireNonNull(getClass().getResource("/projekt/bullet2.png")).toExternalForm()),
                    new Image(Objects.requireNonNull(getClass().getResource("/projekt/bullet3.png")).toExternalForm()),
                    new Image(Objects.requireNonNull(getClass().getResource("/projekt/bullet4.png")).toExternalForm())
            };
            this.entityWidth = 50;
            this.entityHeight = 50;
        }
        else {
            bulletFrames = null;
            bulletFrameIndex = 0;
            bulletFrameTimer = 0.0;
        }
    }

    public boolean collisionFinalEntity(Entity player,Entity bullet) {
        Rectangle2D playerBox = new Rectangle2D(
                player.getEntityX(),
                player.getEntityY(),
                player.getEntityWidth(),
                player.getEntityHeight()
        );

        Rectangle2D bulletBox = new Rectangle2D(
                bullet.getEntityX(),
                bullet.getEntityY(),
                bullet.getEntityWidth(),
                bullet.getEntityHeight()
        );


        return playerBox.intersects(bulletBox);
    }

    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(entityX, entityY, entityWidth, entityHeight);
    }

}
