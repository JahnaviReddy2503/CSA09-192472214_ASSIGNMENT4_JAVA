# Task 06 - Campus Event Registration Desktop App

## Stage 1 - Requirements

### Users and screens

- **Student:** enters student details, chooses an event, registers, filters registrations, and exports visible data.
- **Registration desk operator:** reviews registrations, handles validation messages, and refreshes the list.
- **Main screen:** registration form, event filter, paged registration table, status label, and menu bar.
- **Error dialogs:** explain invalid input, duplicate registration, database failure, or export failure.

### Data

- Student: student ID, name, email, phone.
- Event: event ID, event name, event date, venue.
- Registration: registration ID, student ID, event ID, registration time.

### Rules and testable requirements

| ID | Testable requirement |
|---|---|
| R1 | The application displays registration fields and an event selector. |
| R2 | A registration is rejected when any required field is empty. |
| R3 | A registration is rejected when email does not have a valid basic email shape. |
| R4 | A registration is rejected when phone contains fewer than 7 or more than 15 digits. |
| R5 | A valid registration stores or updates the student and inserts one registration. |
| R6 | The service checks for an existing student/event registration before inserting. |
| R7 | The database `UNIQUE(student_id, event_id)` constraint is the final duplicate safeguard. |
| R8 | The table can be filtered by event. |
| R9 | Registration rows load in pages using SQL `LIMIT` and `OFFSET`. |
| R10 | Database work runs in `SwingWorker`, not on the Event Dispatch Thread. |
| R11 | Refresh reloads the first page and displays status feedback. |
| R12 | Export writes the currently displayed table rows to CSV. |
| R13 | File > Exit closes the window; View > Refresh reloads data. |

## Stage 2 - Screen sketches and flow

```text
+-----------------------------------------------------------------------+
| File [Export, Exit]   View [Refresh]                                  |
+-----------------------------------------------------------------------+
| Campus Event Registration                                             |
| Student ID [________] Name [______________] Email [______________]    |
| Phone [___________] Event [v Select event                      v]      |
| [Register]                                                            |
| Filter event [All events                                      v]       |
+-----------------------------------------------------------------------+
| ID | Student ID | Name | Email | Phone | Event | Registered At         |
|-----------------------------------------------------------------------|
| ... paged registration rows ...                                       |
+-----------------------------------------------------------------------+
| [Previous] [Next]                         Page 1       Status message  |
+-----------------------------------------------------------------------+
```

```text
Open frame
   |
   +--> SwingWorker loads events and first registration page
   |       |
   |       +--> populate form/table and update status
   |
   +--> Register --> validate --> service duplicate check --> DB insert
   |                    |                                  |
   |                    +--> error dialog/status            +--> reload page
   |
   +--> Filter event --> reset page --> SwingWorker loads matching page
   +--> View/Refresh --> SwingWorker reloads page
   +--> File/Export --> save CSV dialog --> write visible rows
   +--> File/Exit or window close --> dispose frame
```

## Stage 3 - Responsibilities and threading

- `RegistrationFrame` reads controls, displays results, and starts workers. It
  does not contain SQL.
- `RegistrationService` trims and validates values, checks duplicates through
  the DAO, and coordinates the registration operation.
- `RegistrationDao` owns prepared SQL, result mapping, transactions, paging,
  and database resource closing.
- `DbConnection` reads `db.properties` and creates JDBC connections.
- The database has `UNIQUE(student_id, event_id)` so a race between two service
  checks cannot create a duplicate. The service check provides a friendly
  early message; the constraint is authoritative.
- Register, refresh, filter, and page operations execute in a `SwingWorker`.
  The worker's `doInBackground()` performs the database call; `done()` updates
  Swing controls on the EDT. The EDT is blocked only for quick form and table
  model updates, not for DB latency.

## Stage 4 - Class diagram and pseudocode

```text
ui
  RegistrationFrame --> RegistrationTableModel
  RegistrationFrame --> RegistrationService
service
  RegistrationService --> RegistrationDao
dao
  RegistrationDao --> DbConnection
  RegistrationDao --> RegistrationRow / EventOption
```

### Register pseudocode

**Algorithm:** RegisterStudent  
**Input:** student ID, name, email, phone, event ID  
**Output:** success or a user-readable failure

1. Trim all text values.
2. Reject an empty required value.
3. Reject an invalid email or phone.
4. Ask `RegistrationDao.exists(studentId, eventId)` whether the pair exists.
5. If it exists, return a duplicate result.
6. Ask the DAO to insert/update the student and insert the registration.
7. Let the database unique constraint reject any concurrent duplicate.
8. Return success; the UI reloads the first page in a `SwingWorker`.

### Filter pseudocode

**Algorithm:** FilterRegistrations  
**Input:** optional event ID, page size, page number  
**Output:** rows for the selected page

1. Set the current filter and reset page number to zero.
2. Start a `SwingWorker`.
3. In `doInBackground`, call DAO paging with `LIMIT pageSize OFFSET page*pageSize`.
4. In `done`, replace the table model rows and update the status label.
5. Enable Previous only when page > 0.
6. Enable Next when returned rows equal the page size.

## Stage 5 - Database and implementation

Run [schema.sql](<C:/Users/janur/Downloads/ASS4 JAVA/Task06/sql/schema.sql>) in
MySQL, copy `db.properties.example` to `db.properties`, and fill in local
credentials. `db.properties` is intentionally not committed with credentials.

### Test-case table

| ID | Type | Input | Expected result | Observed result |
|---|---|---|---|---|
| T1 | Normal | Existing student data, valid email/phone, selected event | Registration succeeds, status confirms it, and the new row appears after refresh. | `<fill observed result>` |
| T2 | Invalid | Blank name or malformed email/phone | Registration is rejected before DB insert with a specific error dialog. | `<fill observed result>` |
| T3 | Duplicate | Register the same student for the same event twice | Second attempt is rejected by service check or unique constraint; only one row exists. | `<fill observed result>` |

### Alternative: all rows versus paged rows

For 100,000 registrations, loading all rows needs one large query and roughly
`O(100000)` row objects plus table-model memory. Initial time and EDT risk are
high if the query or model update is done synchronously; scrolling is quick
afterward but the entire dataset is held in memory.

Paged `LIMIT/OFFSET` loads only `p` rows per request, so memory is `O(p)` and
the UI remains responsive because each request runs in a `SwingWorker`. Initial
work is proportional to one page, although deep offsets can become slower in
some databases because the server still scans/skips earlier rows. For very
large data, keyset pagination can improve deep-page performance. This
assignment uses `LIMIT/OFFSET` because it is clear and directly required.
