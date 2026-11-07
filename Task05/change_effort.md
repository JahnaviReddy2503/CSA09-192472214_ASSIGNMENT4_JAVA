# Version 2 change effort

Each version 2 wrapper reuses the complete version, so the wrapper adds one
class declaration and one delegated `main` call. The actual complete versions
register one handler on the button and amount field and show a dialog. A named
handler and lambda require one shared handler object; frame implementation
centralizes dispatch; anonymous code is concise but less reusable.

For a large application, use a shared `Action` or method reference such as
`button.addActionListener(this::convert)`. One Action can serve a button, menu
item, and keyboard shortcut; adding a trigger is one registration instead of
duplicating logic.
