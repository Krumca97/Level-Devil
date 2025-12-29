package projekt;

import javafx.geometry.Rectangle2D;

public interface Collisionable {
    Rectangle2D getBoundingBox();
    boolean intersects(Rectangle2D other);
    void onCollision(Collisionable other);
}
