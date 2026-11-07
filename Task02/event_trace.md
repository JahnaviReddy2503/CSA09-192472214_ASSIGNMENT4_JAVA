# Event trace

`main` -> constructor -> `setVisible(true)` -> `windowOpened` ->
`windowActivated` -> user `itemStateChanged`/`actionPerformed` events ->
`windowClosing` -> optional confirmation -> `dispose()` -> `windowClosed` ->
`windowDeactivated`.

An applet uses `init`, `start`, repeated `paint`, `stop`, and `destroy`.
A JFrame uses construction, visibility, window callbacks, and disposal instead.
