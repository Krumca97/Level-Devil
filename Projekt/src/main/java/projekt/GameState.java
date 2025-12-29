package projekt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    public double playerX;
    public double playerY;

    public List<double[]> bullets = new ArrayList<>();
    public List<double[]> ledges = new ArrayList<>();

    public int bonus;

}
