package projekt;

import javafx.geometry.Rectangle2D;

public class FinalScene extends Scene {

    private Game game;

    public FinalScene(double x, double y, double width, double height,Game game) {
        super(x, y, width, height);
        this.game = game;
    }

    @Override
    public void onCollision(Collisionable other) {
        if (other instanceof Entity player) {
            Rectangle2D playerBox = player.getBoundingBox();
            Rectangle2D doorBox = getBoundingBox();

            if (playerBox.intersects(doorBox)) {
                game.onLevelFinished();
            }
        }
    }
}
