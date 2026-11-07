# Alternative analysis

`GridBagLayout` offers precise weights in one container but has verbose
constraints. Nested panels have a few more container objects and linear layout
work, but each row has a clear responsibility. Absolute bounds can be slightly
cheaper to compute but fail under resize, DPI, fonts, and localization.
