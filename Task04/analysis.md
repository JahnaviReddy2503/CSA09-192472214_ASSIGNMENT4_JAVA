# DrawPad analysis

`mouseclicked` has a lowercase `c`, so it does not override
`mouseClicked`; without `@Override` the compiler cannot warn. `getGraphics()`
draws directly to a temporary surface, so minimize/restore triggers `paint()`
from scratch and erases dots. The fixed version stores points, updates the
label, calls `repaint()`, and redraws 6x6 ovals in `paint`.

The point list costs `O(n)` memory and `O(n)` per repaint. A
`BufferedImage` costs `O(width*height)` memory and copies one prepared image;
it is fast for repaint but harder to resize/edit individual points.
