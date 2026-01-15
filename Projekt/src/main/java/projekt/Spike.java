package projekt;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Spike implements Collisionable {

    private double x;
    private double y;
    private double size;

    public Spike(double x, double y, double size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.GREEN);

        double[] xs = {x, x + size / 2, x + size};
        double[] ys = {y + size, y, y + size};

        gc.fillPolygon(xs, ys, 3);

        // debug binding box
//        gc.setStroke(Color.BLUE);
//        gc.strokeRect(x, y, size, size);
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(x, y, size, size);
    }

    @Override
    public boolean intersects(Rectangle2D other) {
        return getBoundingBox().intersects(other);
    }

    @Override
    public void onCollision(Collisionable other) {
        //pass
    }
}
