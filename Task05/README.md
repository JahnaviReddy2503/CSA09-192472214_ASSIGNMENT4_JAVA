# Task 5 - Currency Converter

## Aim

Compare four Swing event-handling styles using a currency converter.

## Run

```powershell
javac -d Task05\out Task05\src\*.java
java -cp Task05\out ConverterA
java -cp Task05\out ConverterB
java -cp Task05\out ConverterC
java -cp Task05\out ConverterD
```

Version 2 programs display results in a dialog and also respond to Enter:

```powershell
java -cp Task05\out ConverterD2
```

For example, converting `100 USD` to `EUR` displays approximately `108.70
EUR` using the documented rates. Non-numeric input displays `Invalid amount`.
