package projekt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Renderer {
    private List<Collisionable> entities;
    private Comparator<Collisionable> comparator;

    public Renderer() {
        entities = new ArrayList<>();

        //comparator na lepsi vykreslovani objektu
        comparator = new Comparator<>() {
            @Override
            public int compare(Collisionable o1, Collisionable o2) {
                double y1 = getY(o1);
                double y2 = getY(o2);
                return Double.compare(y2, y1);
            }

            private double getY(Collisionable c) {
                if (c instanceof Entity e){
                    return e.getEntityY();}
                if (c instanceof Scene s){
                    return s.getBoundingBox().getMinY();}
                if (c instanceof FinalScene f){
                    return f.getBoundingBox().getMinY();}
                return 0;
            }
        };

        comparator = comparator.reversed();
    }

    public void addEntity(Collisionable e) {
        entities.add(e);
    }

    public void clearEntities() {
        entities.clear();
    }

    public void render(GraphicsContext gc) {
        entities.sort(comparator);

        for (Collisionable c : entities) {
            if (c instanceof Entity e) {
                e.draw(gc);
            } else if (c instanceof FinalScene f) {
                f.draw(gc, Color.BROWN);
            } else if (c instanceof Scene s) {
                s.draw(gc, Color.GREEN);
            }
        }
    }
}
