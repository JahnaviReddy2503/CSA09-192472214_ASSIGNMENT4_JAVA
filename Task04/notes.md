# Task 04 - Event-Handling Forensics: DrawPad

## 4.1 Why the click is never handled

The method is written as `mouseclicked`, but Java is case-sensitive and
`MouseAdapter` defines `mouseClicked` with a capital `C`. Therefore the
misspelled method is a new method, not an override, and the event-dispatch
system never calls it.

The listener is attached to the `Frame`, while a click on a child `Label` is
delivered to that child rather than automatically bubbling to the frame. Thus
clicks on the label do not reach the frame listener. Even if the correctly
spelled handler receives a click, `count++` only changes an integer; it does not
change the `Label` text. The label must be updated explicitly with
`setText("Clicks: " + count)`.

The completed version uses `@Override`, stores each `Point`, updates the label,
and calls `repaint()` after every click.

## 4.2 Reproducible failing interaction

1. Run the broken/original DrawPad.
2. Click the drawing area several times and observe the temporary dots.
3. Minimise the window and restore it, or cover it completely with another
   window and then uncover it.
4. The dots disappear because `getGraphics()` draws directly on the current
   screen surface. Those pixels are not part of the component's model or
   painting state.
5. AWT repaints the frame after restoring/uncovering it. `paint(Graphics g)`
   starts from the background and, unless it redraws stored points, the old
   dots are gone.

The fix stores points in a list and redraws every stored point from `paint()`.
Calling `repaint()` requests a proper repaint instead of drawing directly with
`getGraphics()`.

## 4.3 Completed implementation

`src/DrawPad.java`:

- Correctly overrides `mouseClicked` with `@Override`.
- Stores each click as a `Point`.
- Updates the label to `Clicks: n`.
- Calls `repaint()`.
- Calls `super.paint(g)` and draws every point as a centred 6x6 filled oval.

## 4.4 Deliberately broken override

`src/DrawPadBroken.java` keeps `@Override` on the misspelled
`mouseclicked` method. Compile it with:

```powershell
javac -d Task04\broken-out Task04\src\DrawPadBroken.java
```

The exact compiler error to record is:

```text
Task04\src\DrawPadBroken.java:8: error: method does not override or implement a method from a supertype
    @Override
    ^
1 error
```

The line number can vary if the file is edited; the important diagnostic text
is `method does not override or implement a method from a supertype`.

## Alternative concept: off-screen image

Instead of storing points and redrawing them, an off-screen
`BufferedImage` can retain the drawing:

```java
private final BufferedImage image = new BufferedImage(
        600, 400, BufferedImage.TYPE_INT_ARGB);

void addPoint(Point point) {
    Graphics2D imageGraphics = image.createGraphics();
    try {
        imageGraphics.fillOval(point.x - 3, point.y - 3, 6, 6);
    } finally {
        imageGraphics.dispose();
    }
    repaint();
}

@Override
public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(image, 0, 0, this);
}
```

Redrawing `n` stored points takes `O(n)` drawing work per repaint and uses
`O(n)` point storage. Copying one prepared image is approximately `O(w*h)`
pixel work for an image of width `w` and height `h` (often optimized by the
graphics system), while the application does not iterate through all points.
The image approach uses `O(w*h)` pixel memory and preserves pixels directly,
but resizing requires a new image or scaling and changing an old point is
harder. The point-list approach is more editable and resolution-independent.

## Practical execution record

**Aim:** Detect, count, store, and repaint mouse clicks.

**Requirements:** Java 17+, AWT `Frame`, `MouseAdapter`, `ArrayList<Point>`,
`paint(Graphics)`, and `WindowAdapter`.

| Test | Input/action | Expected result | Verification |
|---|---|---|---|
| Click | Click drawing area three times | `Clicks: 3` and three dots | Source compiled |
| Repaint | Minimize and restore | Dots remain visible | Code redraws stored points |
| Close | Close window | Frame disposes | Source compiled |
| Broken method | Compile DrawPadBroken | Override diagnostic | Diagnostic verified |
