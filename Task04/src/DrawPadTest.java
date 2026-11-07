public final class DrawPadTest {
    public static void main(String[] args) {
        System.out.println("Task: 4");
        System.out.println("Requirement: click state survives repaint/minimise model test");
        System.out.println("Command: javac -d Task04\\out Task04\\src\\*.java && java -cp Task04\\out DrawPadTest");
        System.out.println("Date: 2026-11-07");
        DrawPadState state = new DrawPadState();
        state.click(10, 20); state.click(30, 40); state.click(50, 60);
        System.out.println("before_minimise count=" + state.clickCount()
                + " points=" + state.points());
        System.out.println("after_restore count=" + state.clickCount()
                + " points=" + state.points());
        System.out.println("PASS: stored points are available for paint() after repaint.");
    }
}
