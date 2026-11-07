import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public final class AppointmentDesk extends JFrame {
    private static final long serialVersionUID = 1L;
    private final AppointmentService service;
    private final JTextField patientIdField = new JTextField();
    private final JTextField patientNameField = new JTextField();
    private final JComboBox<String> departmentBox = new JComboBox<>(
            new String[]{"Cardiology", "Neurology", "General Medicine"});
    private final JComboBox<String> doctorBox = new JComboBox<>();
    private final JComboBox<String> timeSlotBox = new JComboBox<>(
            new String[]{"09:00", "10:00", "11:00", "14:00"});
    private boolean bookingInProgress;

    public AppointmentDesk(AppointmentService service) {
        this.service = service;
        setTitle("Hospital Outpatient Appointment Desk");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(520, 300);
        setLocationRelativeTo(null);
        buildUi();
        attachListeners();
        refreshDoctors();
    }

    private void buildUi() {
        JPanel fields = new JPanel(new GridLayout(5, 2, 8, 8));
        fields.add(new JLabel("Patient ID:"));
        fields.add(patientIdField);
        fields.add(new JLabel("Patient name:"));
        fields.add(patientNameField);
        fields.add(new JLabel("Department:"));
        fields.add(departmentBox);
        fields.add(new JLabel("Doctor:"));
        fields.add(doctorBox);
        fields.add(new JLabel("Time slot:"));
        fields.add(timeSlotBox);

        JButton bookButton = new JButton("Book appointment");
        bookButton.addActionListener(event -> book(bookButton));
        JPanel root = new JPanel(new GridLayout(2, 1, 8, 8));
        root.add(fields);
        root.add(bookButton);
        add(root);
    }

    private void attachListeners() {
        departmentBox.addActionListener(event -> refreshDoctors());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                if (bookingInProgress) {
                    int choice = JOptionPane.showConfirmDialog(
                            AppointmentDesk.this,
                            "A booking is in progress. Close anyway?",
                            "Confirm close",
                            JOptionPane.YES_NO_OPTION);
                    if (choice != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                dispose();
            }
        });
    }

    private void refreshDoctors() {
        String department = (String) departmentBox.getSelectedItem();
        List<String> doctors = service.doctorsFor(department);
        doctorBox.setModel(new DefaultComboBoxModel<>(doctors.toArray(String[]::new)));
    }

    private void book(JButton bookButton) {
        bookingInProgress = true;
        bookButton.setEnabled(false);
        String patientId = patientIdField.getText();
        String patientName = patientNameField.getText();
        String department = (String) departmentBox.getSelectedItem();
        String doctor = (String) doctorBox.getSelectedItem();
        String timeSlot = (String) timeSlotBox.getSelectedItem();
        new SwingWorker<Appointment, Void>() {
            @Override
            protected Appointment doInBackground() {
                return service.validateAndBook(patientId, patientName, department, doctor, timeSlot);
            }

            @Override
            protected void done() {
                try {
                    Appointment appointment = get();
                    JOptionPane.showMessageDialog(AppointmentDesk.this,
                            "Appointment confirmed for " + appointment.patientName()
                                    + " with " + appointment.doctor()
                                    + " at " + appointment.timeSlot(),
                            "Booking successful",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception exception) {
                    String message = exception.getCause() instanceof IllegalArgumentException
                            ? exception.getCause().getMessage()
                            : "Booking could not be completed.";
                    JOptionPane.showMessageDialog(AppointmentDesk.this, message,
                            "Booking error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    bookingInProgress = false;
                    bookButton.setEnabled(true);
                }
            }
        }.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppointmentDesk(new AppointmentService()).setVisible(true));
    }
}
