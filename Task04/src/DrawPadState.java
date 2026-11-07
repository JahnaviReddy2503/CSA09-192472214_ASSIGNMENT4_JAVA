import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public final class DrawPadState {
    private final List<Point> points = new ArrayList<>();

    public void click(int x, int y) {
        points.add(new Point(x, y));
    }

    public int clickCount() {
        return points.size();
    }

    public List<Point> points() {
        return List.copyOf(points);
    }
}
