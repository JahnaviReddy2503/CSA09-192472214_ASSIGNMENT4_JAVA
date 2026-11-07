# CSA09 Programming in Java - Assignment 4 (CO4)

## Student details

- **Name:** `<fill>`
- **Register No:** `192472214`
- **Java version:** Java 17+ (tested with Java 17)
- **GUI technology:** AWT and Swing
- **Repository name:** `CSA09-192472214_ASSIGNMENT4_JAVA`
- **GitHub repository:** [CSA09-192472214_ASSIGNMENT4_JAVA](https://github.com/JahnaviReddy2503/CSA09-192472214_ASSIGNMENT4_JAVA.git)

## Requirements and conventions

- Compile and run with plain `javac` and `java`.
- Swing user interfaces are created with `SwingUtilities.invokeLater`.
- Database and other long-running work is kept off the Event Dispatch Thread.
- Task 6 uses `PreparedStatement`, try-with-resources, and a database unique
  constraint for duplicate protection.
- Each task has separate `src`, `notes.md`, and `screenshots` content.

## Task 1 - Fee Window redesign

```powershell
javac -d Task01\out Task01\src\*.java
java -cp Task01\out Main
```

**Result:** Implemented a layered fee-payment Swing application with
`FeeWindow`, `FeeService`, repository abstraction, validation, receipt display,
status feedback, `SwingWorker`, and window-closing handling. The default
in-memory repository runs without MySQL.

## Task 2 - Hospital outpatient appointment desk

Design-only task. No Java compilation is required.

**Result:** Documented the event table, component hierarchy, pseudocode,
window lifecycle, applet comparison, and database-fetch versus cached-map
analysis in [Task02/notes.md](./Task02/notes.md).

## Task 3 - Campus library search screen

```powershell
javac -d Task03\out Task03\src\*.java
java -cp Task03\out Main
```

**Result:** Implemented a resizable Swing library search screen with nested
layout managers, dummy search data, category filtering, result navigation,
reservation feedback, and a status label.

## Task 4 - DrawPad event-handling forensics

```powershell
javac -d Task04\out Task04\src\DrawPad.java
java -cp Task04\out DrawPad
```

To reproduce the intentional compiler diagnostic:

```powershell
javac -d Task04\broken-out Task04\src\DrawPadBroken.java
```

**Result:** Implemented persistent point drawing with correct
`mouseClicked`, `repaint`, and `paint` behavior. The broken override example
and exact compiler diagnostic are documented in [Task04/notes.md](./Task04/notes.md).

## Task 5 - Currency converter event-handling styles

```powershell
javac -d Task05\out Task05\src\*.java
java -cp Task05\out ConverterD
```

Optional version-2 runs:

```powershell
java -cp Task05\out ConverterA2
java -cp Task05\out ConverterB2
java -cp Task05\out ConverterC2
java -cp Task05\out ConverterD2
```

**Result:** Implemented frame-listener, named-inner-class, anonymous-class,
and lambda versions, plus Enter-key and dialog versions. Comparison tables and
change counts are in [Task05/notes.md](./Task05/notes.md).

## Task 6 - Campus event registration desktop app

1. Install MySQL and the MySQL Connector/J jar.
2. Run [Task06/sql/schema.sql](./Task06/sql/schema.sql) in MySQL.
3. Copy `Task06\db.properties.example` to `Task06\db.properties`.
4. Edit `Task06\db.properties` with local database URL, username, and password.
5. Compile and run:

```powershell
javac -cp "<path-to-mysql-connector-j.jar>" -d Task06\out Task06\src\*.java
java -cp "Task06\out;<path-to-mysql-connector-j.jar>" Main
```

**Result:** Implemented layered Swing/JDBC registration with validation,
duplicate checking, event filtering, CSV export, paged `LIMIT/OFFSET` loading,
menus, status feedback, and `SwingWorker` database operations. Runtime
verification requires a configured local MySQL database.

## Folder checklist

Each task contains:

```text
TaskXX/
├── src/                 Java source files (empty only for design-only Task 2)
├── screenshots/
│   └── README.md        Screenshot placeholder list
└── notes.md             Written answers and test evidence
```

Task 6 additionally contains `sql/schema.sql`, `db.properties.example`, and a
local `db.properties` template. Never commit real database credentials.

## Submission checklist

- Replace `<fill>` with the student's name.
- Add the requested screenshots to each task’s `screenshots` folder.
- Fill the observed-result columns in Task 3 and Task 6 notes.
- Add the final GitHub repository URL to the report cover page.
- Use one commit per task as shown in the submission command sequence.
