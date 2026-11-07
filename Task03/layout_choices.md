# Layout choices

The frame uses `BorderLayout`: NORTH contains title/search controls, CENTER
contains the growing results scroll pane, and SOUTH contains fixed navigation
controls and status. Nested `FlowLayout` keeps search and button rows at
preferred sizes. A `GridBagLayout` could use weights but would require more
constraints. `setLayout(null)` is fragile under resizing and font/DPI changes.
Each layout pass is approximately `O(n)` for `n` components; nested panels use
more containers but provide predictable resizing.

```text
JFrame BorderLayout
├── NORTH JPanel BorderLayout -> title + search FlowLayout
├── CENTER JScrollPane -> JTextArea
└── SOUTH JPanel GridLayout -> buttons FlowLayout + status
```
