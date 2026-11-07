# CSA09 Programming in Java - Assignment 4

- **Register number:** `<REGNO>`
- **Java:** 17
- **Database:** MySQL (Task 6)
- **GitHub:** `https://github.com/<USERNAME>/CSA09-<REGNO>-Assignment4`

## Compile and run

PowerShell commands from the repository root:

```powershell
javac -d Task01\out Task01\src\*.java
java -cp Task01\out FeeServiceTest

javac -d Task02\out Task02\src\*.java
java -cp Task02\out AppointmentDesk --test

javac -d Task03\out Task03\src\*.java
java -cp Task03\out LibrarySearch --test

javac -d Task04\out Task04\src\*.java
java -cp Task04\out DrawPadTest

javac -d Task05\out Task05\src\*.java
java -cp Task05\out ConverterTest
```

Task 6 requires MySQL Connector/J:

```powershell
javac -cp "<path-to-mysql-connector-j.jar>" -d Task06\out Task06\src\*.java
java -cp "Task06\out;<path-to-mysql-connector-j.jar>" app.Main
```

Run `Task06/sql/schema.sql` first and copy `Task06/db.properties.example` to
`Task06/db.properties`. Do not commit credentials.

## Results

- **Task 1:** Fee validation and mock payment tests execute from the console.
- **Task 2:** Appointment event trace and mock booking tests execute from the console.
- **Task 3:** Search behavior and three programmatic resize measurements execute.
- **Task 4:** Click model and repaint-state harness execute; GUI requires a desktop.
- **Task 5:** Four converter logic styles are tested through one headless runner.
- **Task 6:** Mock service tests execute; MySQL execution is marked NOT EXECUTED
  when MySQL/Connector/J are unavailable.

## Structure

Each task has `src`, `output`, and documentation. Task 6 also has `docs`,
`sql`, and `db.properties.example`. No screenshots are used.
