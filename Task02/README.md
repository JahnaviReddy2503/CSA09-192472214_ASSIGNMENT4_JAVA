# Task 2 - Hospital Outpatient Appointment Desk

## Aim

Implement a department-driven appointment desk and explain its event and
window lifecycle.

## Run

```powershell
javac -d Task02\out Task02\src\*.java
java -cp Task02\out AppointmentDesk
```

## Sample execution

Enter patient ID `P1001`, name `Asha Rao`, choose Cardiology, Dr. Meera, and
09:00, then click **Book appointment**. A confirmation dialog is shown.
Blank patient details produce an error dialog. Changing department refreshes
the doctor list.

## Verification

Working sources compile with Java 17. Service smoke tests cover a valid booking
and rejected blank patient name. The design answers are in `notes.md`.
