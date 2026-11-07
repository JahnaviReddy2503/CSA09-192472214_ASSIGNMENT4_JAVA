# Task 6 - Campus Event Registration

## Aim

Build a layered Swing/JDBC/MySQL registration application with validation,
duplicate protection, filtering, CSV export, and paged background loading.

## Database setup

1. Run `sql/schema.sql` in MySQL.
2. Copy `db.properties.example` to `db.properties`.
3. Replace the local username and password.

## Compile and run

```powershell
javac -cp "<path-to-mysql-connector-j.jar>" -d Task06\out Task06\src\*.java
java -cp "Task06\out;<path-to-mysql-connector-j.jar>" Main
```

## Sample execution

Use student ID `ST101`, name `Asha Rao`, email `asha@example.com`, phone
`9876543210`, and select an event. Click **Register**. The table reloads with
the registration. Repeating the same student/event is rejected. Invalid email
or phone is rejected before the insert. File > Export writes the visible page
as CSV.

## Verification

Java source compilation was verified without the external JDBC jar because the
JDBC types are part of the JDK. Full runtime verification requires MySQL and
Connector/J configured locally.
