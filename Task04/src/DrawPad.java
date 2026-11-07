import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public final class DrawPad extends Frame {
    private static final long serialVersionUID = 1L;
    private final Label status = new Label("Clicks: 0");
    private final List<Point> points = new ArrayList<>();

    public DrawPad() {
        setTitle("DrawPad");
        setSize(600, 400);
        add(status, "North");
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                points.add(event.getPoint());
                status.setText("Clicks: " + points.size());
                repaint();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent event) { dispose(); }
        });
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        for (Point point : points) {
            graphics.fillOval(point.x - 3, point.y - 3, 6, 6);
        }
    }

    public static void main(String[] args) {
        new DrawPad().setVisible(true);
    }
}
