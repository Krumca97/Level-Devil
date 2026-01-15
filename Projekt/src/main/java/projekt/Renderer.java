package projekt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Renderer {
    private Comparator<Collisionable> comparator;

    public Renderer() {

        comparator = new Comparator<>() {
            @Override
            public int compare(Collisionable o1, Collisionable o2) {
                double y1 = getY(o1);
                double y2 = getY(o2);
                return Double.compare(y2, y1);
            }

            private double getY(Collisionable c) {
                if (c instanceof Entity e){
                    return e.getEntityY();
                }

                if (c instanceof Scene s){
                    return s.getBoundingBox().getMinY();
                }

                if (c instanceof FinalScene f){
                    return f.getBoundingBox().getMinY();
                }

                return 0;
            }
        };
        comparator = comparator.reversed();
    }
}
