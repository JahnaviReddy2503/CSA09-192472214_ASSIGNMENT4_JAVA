# Task 4 - DrawPad Event Handling

## Aim

Repair mouse event handling and preserve click drawings through repainting.

## Run

```powershell
javac -d Task04\out Task04\src\DrawPad.java
java -cp Task04\out DrawPad
```

Click the drawing area several times. The label increments and each click
produces a filled dot. Minimize and restore to verify that stored points are
redrawn.

The intentional compiler exercise is:

```powershell
javac -d Task04\broken-out Task04\src\DrawPadBroken.java
```

It reports that the misspelled method does not override a supertype method.
