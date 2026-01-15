package projekt;

import javafx.scene.canvas.GraphicsContext;

public interface Level {
    void init(Game game);
    void update(double delta);
    void render(GraphicsContext gc);

    double getStartX();
    double getStartY();
}
