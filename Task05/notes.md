# Task 05 - Event-Handling Styles: Currency Converter

## 5.1 Four converter versions

All versions contain an amount field, From and To currency combo boxes, a
Convert button, a result label, and a `WindowAdapter` for closing.

| Version | Handler style |
|---|---|
| `ConverterA` | The frame implements `ActionListener`; `actionPerformed` is the handler. |
| `ConverterB` | A named `ConvertHandler` inner class implements `ActionListener`. |
| `ConverterC` | An anonymous `ActionListener` is registered on the button. |
| `ConverterD` | A lambda `ActionListener` is registered on both the button and amount field. |

`ConverterD.convert()` parses the amount, formats the result to two decimal
places, and displays `Invalid amount` for `NumberFormatException`.

## 5.2 Version 2 changes

Version 2 files are `ConverterA2`, `ConverterB2`, `ConverterC2`, and
`ConverterD2`. Pressing Enter in the amount field performs conversion, and the
converted result or invalid-input message appears in a `JOptionPane`.

Line counts below compare each compact version-2 source with its version-1
counterpart. They are approximate source-line changes grouped by action; blank
and formatting-only changes are excluded.

| Version | Lines added | Lines changed | Lines removed | Main changes |
|---|---:|---:|---:|---|
| A2 | 2 | 2 | 1 | Register the same handler on the text field; show a dialog after conversion. |
| B2 | 3 | 2 | 8 | Reuse the named handler for two triggers; show the dialog and omit the result label. |
| C2 | 3 | 2 | 8 | Reuse the anonymous handler for two triggers; show the dialog. |
| D2 | 3 | 2 | 8 | Reuse the lambda for two triggers; show the dialog. |

The exact count depends on whether a formatter counts blank lines and compact
statements, so the table states the comparison convention explicitly.

## 5.3 Comparison and recommendation

| Style | Readability | Access to frame fields | Handler reuse | Ease of change |
|---|---|---|---|---|
| Frame implements listener | Clear for tiny classes, but mixes frame and event roles. | Direct access. | Easy to reuse one handler, but all events enter one method. | Can become difficult as events grow. |
| Named inner class | Very clear and gives the handler a meaningful name. | Direct access to outer fields. | Good for multiple controls and unit-focused changes. | Good for medium-sized screens. |
| Anonymous inner class | Keeps logic near registration, but verbose for longer handlers. | `OuterClass.this` may be needed. | Reuse requires storing the anonymous object. | Less convenient for large handlers. |
| Lambda | Concise and readable for short actions. | Captures effectively final local/outer fields. | Easy to register the same lambda on multiple triggers. | Excellent for small independent actions. |

For a large application, use named command classes or Swing `Action` objects
behind a service layer. They provide names, shared enable/disable state,
keyboard/menu reuse, and testable boundaries without putting every event into
the frame class. Lambdas remain ideal for short, local UI-only actions.

## Alternative concept

A method reference or shared `Action` can centralize one conversion command:

```java
button.addActionListener(this::convert);
amountField.addActionListener(this::convert);

Action shared = new AbstractAction("Convert") {
    @Override
    public void actionPerformed(ActionEvent event) {
        convert();
    }
};
button.setAction(shared);
menuItem.setAction(shared);
inputMap.put(KeyStroke.getKeyStroke("ENTER"), "convert");
actionMap.put("convert", shared);
```

With four separate registrations, each style can use one listener object per
trigger unless it explicitly shares a handler. A method reference usually
creates one reusable listener object for the registered target(s), while one
`Action` object can serve a button, menu item, and keyboard shortcut. Adding a
new trigger is one registration in either approach; the shared `Action` also
keeps enabled state, label, and accelerator behavior consistent. The work to
add a trigger is therefore lower and less error-prone with the shared `Action`,
especially as the application grows.

## Practical execution record

**Aim:** Convert an amount using four event-handling styles.

**Requirements:** Java 17+, Swing, `ActionListener`, inner classes, anonymous
classes, lambdas, and `JOptionPane`.

| Test | Input/action | Expected result | Verification |
|---|---|---|---|
| Valid conversion | 100 USD to EUR | `Result: 108.70 EUR` | Conversion smoke test passed |
| Invalid input | `bad` | `Invalid amount` | Conversion smoke test passed |
| Enter key | Type amount and press Enter | Version 2 shows dialog | Source implements listener |
| Window close | Close any converter | Frame disposes | Source compiled |
