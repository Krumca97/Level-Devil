package projekt;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Scene {
    //pozice a velikost
    private final double platformX;
    private final double platformY;
    private final double platformWidth;
    private final double platformHeight;

    public Scene(double platformX, double platformY, double platformWidth, double platformHeight) {
        this.platformX = platformX;
        this.platformY = platformY;
        this.platformWidth = platformWidth;
        this.platformHeight = platformHeight;
    }

    public void draw(GraphicsContext gc,Color color) {
        gc.setFill(color);
        gc.fillRect(platformX, platformY, platformWidth, platformHeight);
    }

    //detekce kolize
    public void collision(Entity player){
        Rectangle2D playerBox = new Rectangle2D(
                player.getEntityX(),
                player.getEntityY(),
                player.getEntityWidth(),
                player.getEntityHeight()
        );

        Rectangle2D platformBox = new Rectangle2D(
                platformX,
                platformY,
                platformWidth,
                platformHeight
        );


        if (playerBox.intersects(platformBox)) {
            if (player.getEntityVelocityY() >= 0) {
                player.setEntityY(platformY - player.getEntityHeight());
                player.setEntityVelocityY(0);
                player.setOnGround(true);
            }
        }
    }
    //detekce ukonceni hry
    public boolean collisionFinal(Entity player) {
        Rectangle2D playerBox = new Rectangle2D(
                player.getEntityX(),
                player.getEntityY(),
                player.getEntityWidth(),
                player.getEntityHeight()
        );

        Rectangle2D platformBox = new Rectangle2D(
                platformX,
                platformY,
                platformWidth,
                platformHeight
        );


        if (playerBox.intersects(platformBox)) {
            if (player.getEntityVelocityY() >= 0) {
                return true;
            }
        }
        return false;
    }

    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(platformX, platformY, platformWidth, platformHeight);
    }

    public double getPlatformX(){
        return platformX;
    }
    public double getPlatformY(){
        return platformY;
    }
    public double getPlatformWidth(){
        return platformWidth;
    }
    public double getPlatformHeight(){
        return platformHeight;
    }
}
