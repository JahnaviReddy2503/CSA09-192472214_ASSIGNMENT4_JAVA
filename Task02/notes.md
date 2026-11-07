# Task 02 - Hospital Outpatient Appointment Desk

## 2.1 Event table

| Event source | Event object | Listener interface | Action taken |
|---|---|---|---|
| Department `JComboBox<String>` | `ActionEvent` | `ActionListener` | Read the selected department and refresh the doctor list. |
| Doctor `JComboBox<String>` | `ActionEvent` | `ActionListener` | Read the selected doctor and refresh available time slots if slots depend on the doctor. |
| Time-slot `JComboBox<String>` | `ActionEvent` | `ActionListener` | Store the selected appointment time and update any summary display. |
| Book `JButton` | `ActionEvent` | `ActionListener` | Validate patient details and selections; book the appointment or show an error message. |
| Clear `JButton` | `ActionEvent` | `ActionListener` | Clear patient fields and restore the default department, doctor, and time-slot selections. |
| Appointment `JTable` or list refresh | `ListSelectionEvent` | `ListSelectionListener` | Display details of the selected existing appointment, if this feature is enabled. |
| Main `JFrame` | `WindowEvent` | `WindowAdapter` / `WindowListener` | On `windowClosing`, release resources and dispose the frame. |
| Main `JFrame` | `WindowEvent` | `WindowAdapter` / `WindowListener` | On `windowOpened`, optionally focus the patient-name field and load initial data. |

`JComboBox` is used instead of the older AWT `Choice` because Swing provides
consistent rendering and model support. If AWT `Choice` is required, its
selection event can be handled with `ItemListener` and `ItemEvent`.

## 2.2 Component hierarchy

```text
AppointmentDeskFrame : javax.swing.JFrame
└── contentPane : javax.swing.JPanel
    └── mainPanel : javax.swing.JPanel
        ├── headingLabel : javax.swing.JLabel
        ├── patientPanel : javax.swing.JPanel
        │   ├── patientNameLabel : javax.swing.JLabel
        │   ├── patientNameField : javax.swing.JTextField
        │   ├── patientIdLabel : javax.swing.JLabel
        │   └── patientIdField : javax.swing.JTextField
        ├── selectionPanel : javax.swing.JPanel
        │   ├── departmentLabel : javax.swing.JLabel
        │   ├── departmentCombo : javax.swing.JComboBox<String>
        │   ├── doctorLabel : javax.swing.JLabel
        │   ├── doctorCombo : javax.swing.JComboBox<String>
        │   ├── timeSlotLabel : javax.swing.JLabel
        │   └── timeSlotCombo : javax.swing.JComboBox<String>
        ├── bookButton : javax.swing.JButton
        ├── clearButton : javax.swing.JButton
        └── statusLabel : javax.swing.JLabel
```

The frame uses a `BorderLayout`; `patientPanel` and `selectionPanel` may use
`GridBagLayout` or `GridLayout` for their labels and input controls.

## 2.3 Pseudocode

### (a) Refreshing the doctor list when the department changes

**Algorithm:** RefreshDoctors  
**Input:** selected department, `doctorsByDepartment` map  
**Output:** doctor combo box containing only doctors in the selected department

1. Read the selected department from `departmentCombo`.
2. Clear all entries from `doctorCombo`.
3. If no department is selected, disable `doctorCombo` and stop.
4. Find the doctor list using `doctorsByDepartment.get(department)`.
5. If the list is missing or empty, add `"No doctors available"` and disable the combo.
6. Otherwise, add each doctor name to `doctorCombo`.
7. Enable `doctorCombo` and select the first doctor.
8. Refresh the time-slot combo for the selected doctor.
9. Update the status label with the number of available doctors.

### (b) Validating and booking the appointment

**Algorithm:** ValidateAndBookAppointment  
**Input:** patient details, selected department, doctor, and time slot  
**Output:** confirmed appointment or an error message

1. Trim the patient name and patient ID.
2. If the name is empty, show `"Patient name is required"` and stop.
3. If the patient ID is empty, show `"Patient ID is required"` and stop.
4. Read the selected department, doctor, and time slot.
5. If any selection is missing or represents an unavailable item, show a specific error and stop.
6. Disable the Book button to prevent duplicate clicks.
7. Create an appointment object from the validated values.
8. Save the appointment through the service/repository layer, not directly from the UI.
9. If saving succeeds, show the confirmation number and clear or reset the form.
10. If saving fails, show the failure reason and allow the user to retry.
11. Re-enable the Book button in both success and failure paths.

## 2.4 Window event and applet lifecycle trace

### JFrame method-call order

1. `main()` creates the application objects.
2. The constructor creates controls, lays out the panels, and registers listeners.
3. `setSize()` or `pack()` calculates the initial frame size.
4. `setLocationRelativeTo(null)` positions the frame.
5. `setVisible(true)` requests that the native window be displayed.
6. The AWT event queue sends `windowOpened` after the window becomes visible.
7. Focus activation normally causes `windowActivated`; focus events may follow.
8. User events call component listeners, such as `actionPerformed`.
9. When the user closes the window, `windowClosing` is sent first.
10. The handler saves/cancels pending work, releases resources, and calls `dispose()`.
11. `dispose()` removes the native peer and causes `windowClosed`.
12. If another window receives focus, `windowDeactivated` may occur before closing;
    ordering of activation/deactivation can vary with the window manager.

`windowOpened`, `windowActivated`, and `windowDeactivated` are notifications;
they are not guaranteed to occur in exactly the same order in every operating
system scenario. `windowClosing` is the point at which application code should
decide whether closing is allowed.

### Comparison with applet lifecycle

An applet traditionally receives `init()` once for initialization, `start()` when
it becomes active, `paint(Graphics)` whenever it must render, `stop()` when it
is no longer active, and `destroy()` before removal. A `JFrame` instead uses a
constructor for object setup, `setVisible(true)` for showing the window,
listener callbacks for window and user events, and `dispose()`/`windowClosed`
for cleanup. A frame does not have an applet-style `paint` lifecycle contract;
Swing repaints components through `paintComponent`.

## Alternative concept: database fetch versus loading once

Assume `d` departments and `m` total doctors.

### Fetch doctors on every department change

```java
void refreshDoctorsFromDatabase(String department) {
    doctorCombo.removeAllItems();
    String sql = "SELECT name FROM doctors WHERE department = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, department);
        try (ResultSet results = statement.executeQuery()) {
            while (results.next()) {
                doctorCombo.addItem(results.getString("name"));
            }
        }
    }
}
```

Each change has database/network latency plus `O(r)` transfer and UI work for
the `r` doctors in that department. It uses approximately `O(r)` temporary
memory and always reflects current database data, but repeated changes can be
slow and can overload the database.

### Load once into `Map<String, List<Doctor>>`

```java
Map<String, List<Doctor>> doctorsByDepartment = loadAllDoctorsOnce();

void refreshDoctorsFromCache(String department) {
    doctorCombo.removeAllItems();
    for (Doctor doctor : doctorsByDepartment.getOrDefault(department, List.of())) {
        doctorCombo.addItem(doctor.name());
    }
}
```

The initial load costs one query and `O(m)` time; it stores `O(m)` doctors plus
`O(d)` map entries. Each later department change is `O(r)` to repopulate the
combo, without database latency. Caching is preferable for a small, mostly
stable outpatient directory; periodic refresh or an explicit Refresh button is
needed when doctor data changes frequently. If the database is authoritative
for availability, fetch or verify the selected slot during booking as well.

## Runnable implementation

`src/AppointmentDesk.java` is the working Swing implementation. Run:

```powershell
javac -d Task02\out Task02\src\*.java
java -cp Task02\out AppointmentDesk
```

Sample input:

| Control | Value |
|---|---|
| Patient ID | `P1001` |
| Patient name | `Asha Rao` |
| Department | `Cardiology` |
| Doctor | `Dr. Meera` |
| Time slot | `09:00` |

Click **Book appointment** to receive a confirmation dialog. Blank patient
fields or an unavailable doctor produce an error dialog. Changing Department
refreshes the Doctor combo box. The implementation uses `SwingWorker` for the
booking operation and asks for confirmation if the window is closed while a
booking worker is active.
