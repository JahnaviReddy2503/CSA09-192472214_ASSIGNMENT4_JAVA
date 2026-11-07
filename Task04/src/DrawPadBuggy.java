import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DrawPadBuggy extends Frame {
    private static final long serialVersionUID = 1L;
    private final Label status = new Label("Clicks: 0");

    public DrawPadBuggy() {
        add(status, "North");
        addMouseListener(new MouseAdapter() {
            public void mouseclicked(MouseEvent event) {
                getGraphics().fillOval(event.getX(), event.getY(), 6, 6);
            }
        });
    }

    @Override public void paint(Graphics graphics) {
        super.paint(graphics);
    }
}
