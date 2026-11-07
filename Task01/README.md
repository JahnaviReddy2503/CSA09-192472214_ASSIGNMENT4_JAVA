# Task 1 - Fee Payment Window

## Aim

Review a defective event-driven fee window and replace it with a responsive,
layered Swing implementation.

## Requirements

Java 17+, Swing, event listeners, `SwingWorker`, validation, and an optional
JDBC repository using `PreparedStatement`.

## Run

```powershell
javac -d Task01\out Task01\src\*.java
java -cp Task01\out Main
```

## Sample execution

Enter `Asha Rao`, `CSE01`, and `1250`, then click **Pay Fee**. The status shows
successful payment; **Receipt** displays the receipt and **Clear** resets the
form. Entering `bad` as the amount shows a validation message.

## Verification

Working sources compile with Java 17. Service smoke tests cover valid, invalid,
and negative amounts. GUI interaction must be performed in a desktop session.
