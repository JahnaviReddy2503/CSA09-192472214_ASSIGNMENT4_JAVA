# Task 03 - Campus Library Search Screen

## 3.1 Layout-manager choices

| Container | Layout manager | Justification |
|---|---|---|
| Main frame | `BorderLayout` | The center results area receives all extra width and height, while the top and bottom regions retain their preferred heights. |
| Title panel | `FlowLayout` | Centers the title without imposing a fixed size on the whole window. |
| Search panel | `FlowLayout(LEFT)` | Keeps the search field, category selector, and Search button in one compact row. The row can move or wrap naturally as the window becomes narrower. |
| Top panel | `BorderLayout` | Groups the title and search rows into one `NORTH` region without the two rows replacing one another. |
| Bottom panel | `BorderLayout` | Keeps the button row in the center and the status label below it; its preferred height remains stable. |
| Bottom button panel | `FlowLayout(CENTER)` | Groups Prev, Next, and Reserve buttons and keeps them at their preferred sizes. |
| Results area | `JScrollPane` in `BorderLayout.CENTER` | Allows the `JTextArea` to grow with the window and provides scrolling when results exceed the visible area. |
| Optional alternate views | `CardLayout` | Suitable if the screen later gains separate search, reservation, and confirmation cards; it is not needed for the current single-results view. |

The key resize requirement is satisfied by putting only the scroll pane in the
frame's `CENTER`. `BorderLayout` gives that region remaining space, while the
search and bottom rows use their preferred heights in `NORTH`/`PAGE_START` and
`SOUTH`.

## 3.2 Nested container diagram

```text
LibrarySearchWindow : JFrame
└── content pane : JPanel
    └── BorderLayout
        ├── NORTH : topPanel : JPanel
        │   └── BorderLayout
        │       ├── NORTH : titlePanel : JPanel
        │       │   └── FlowLayout(CENTER)
        │       │       └── titleLabel : JLabel
        │       └── SOUTH : searchPanel : JPanel
        │           └── FlowLayout(LEFT)
        │               ├── searchLabel : JLabel
        │               ├── searchField : JTextField
        │               ├── categoryLabel : JLabel
        │               ├── categoryBox : JComboBox<String>
        │               └── searchButton : JButton
        ├── CENTER : resultsScrollPane : JScrollPane
        │   └── resultsArea : JTextArea
        └── SOUTH : bottomPanel : JPanel
            └── BorderLayout
                ├── CENTER : buttonPanel : JPanel
                │   └── FlowLayout(CENTER)
                │       ├── previousButton : JButton
                │       ├── nextButton : JButton
                │       └── reserveButton : JButton
                └── SOUTH : statusLabel : JLabel
```

## 3.3 Implementation behaviour

The screen uses dummy `LibraryBook` records. Search filters by title, author,
and category. Prev and Next move the selected result marker, Reserve reports
success for an available book and a checked-out message otherwise. The status
label reports result count and actions. Swing creation is started through
`SwingUtilities.invokeLater`.

## 3.4 Resize screenshot plan

Take exactly these three screenshots:

1. **800 x 500 pixels** - normal working-size window with all controls and
   several results visible.
2. **1200 x 700 pixels** - enlarged window; the results area should visibly grow
   while the search and button rows remain compact.
3. **600 x 400 pixels** - reduced window; controls remain usable and the
   results area stays scrollable rather than being manually positioned.

| Region | Expected behaviour | Observed |
|---|---|---|
| Title row | Remains at the top with stable preferred height. | `<fill from 800 x 500 screenshot>` |
| Search row | Controls remain in one compact top row where space permits. | `<fill from screenshots>` |
| Results area | Expands most when the window grows and scrolls when content does not fit. | `<fill from screenshots>` |
| Button row | Prev, Next, and Reserve retain preferred button sizes at the bottom. | `<fill from screenshots>` |
| Status label | Remains below the button row and displays the current result/action. | `<fill from screenshots>` |

## Alternative concept comparison

### GridBagLayout

`GridBagLayout` can implement the whole screen in one container with explicit
weights and fill rules. It provides precise resizing control, but its
constraints are more verbose and harder to maintain than small nested panels.
Its layout computation is generally `O(n)` for `n` components, as is the
practical computation for the nested layout managers; real UI time is usually
dominated by painting and text measurement. One GridBag container uses fewer
panel objects, while the nested design uses several small, focused containers.

```java
JPanel panel = new JPanel(new GridBagLayout());
GridBagConstraints c = new GridBagConstraints();
c.gridx = 0; c.gridy = 0;
c.weightx = 1.0; c.fill = GridBagConstraints.HORIZONTAL;
panel.add(searchField, c);
c.gridy = 1;
c.weighty = 1.0; c.fill = GridBagConstraints.BOTH;
panel.add(new JScrollPane(resultsArea), c);
```

### `setLayout(null)` and `setBounds`

Absolute positioning can have a small amount of layout computation and may use
fewer containers, but it does not adapt to font changes, DPI scaling,
localization, or window resizing. Components can overlap or become clipped.
Nested `BorderLayout`/`FlowLayout` panels compute preferred sizes and adapt
automatically, so they are more reliable for this resizable screen even if
they create a few additional `JPanel` objects.
