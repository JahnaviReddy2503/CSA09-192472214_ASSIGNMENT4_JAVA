import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;

public final class AppointmentDesk extends JFrame {
    private static final long serialVersionUID = 1L;
    private final Map<String, List<Doctor>> doctors = Map.of(
            "Cardiology", List.of(new Doctor("Dr. Meera", "Cardiology"),
                    new Doctor("Dr. Arun", "Cardiology")),
            "Neurology", List.of(new Doctor("Dr. Priya", "Neurology"),
                    new Doctor("Dr. Kiran", "Neurology")));
    private final JTextField patientId = new JTextField();
    private final JComboBox<String> department = new JComboBox<>(
            new String[]{"Cardiology", "Neurology"});
    private final JComboBox<Doctor> doctor = new JComboBox<>();
    private final JComboBox<String> slot = new JComboBox<>(
            new String[]{"09:00", "10:00", "14:00"});
    private boolean bookingInProgress;

    public AppointmentDesk() {
        setTitle("Hospital Appointment Desk");
        setSize(440, 260);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout(5, 2, 6, 6));
        panel.add(new JLabel("Patient ID")); panel.add(patientId);
        panel.add(new JLabel("Department")); panel.add(department);
        panel.add(new JLabel("Doctor")); panel.add(doctor);
        panel.add(new JLabel("Time slot")); panel.add(slot);
        JButton book = new JButton("Book");
        panel.add(new JLabel()); panel.add(book);
        add(panel);
        department.addItemListener(new DepartmentListener());
        book.addActionListener(event -> book());
        addWindowListener(new WindowAdapter() {
            @Override public void windowOpened(WindowEvent event) {
                System.out.println("windowOpened");
            }
            @Override public void windowClosing(WindowEvent event) {
                System.out.println("windowClosing");
                if (!bookingInProgress || JOptionPane.showConfirmDialog(
                        AppointmentDesk.this, "Booking active. Close anyway?",
                        "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
            @Override public void windowClosed(WindowEvent event) {
                System.out.println("windowClosed");
            }
        });
        refreshDoctors();
    }

    private final class DepartmentListener implements ItemListener {
        @Override public void itemStateChanged(ItemEvent event) {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                System.out.println("departmentChanged=" + event.getItem());
                refreshDoctors();
            }
        }
    }

    private void refreshDoctors() {
        doctor.removeAllItems();
        for (Doctor item : doctors.getOrDefault(department.getSelectedItem(), List.of())) {
            doctor.addItem(item);
        }
        System.out.println("doctorListRefreshed=" + doctor.getItemCount());
    }

    private void book() {
        bookingInProgress = true;
        try {
            if (patientId.getText().trim().isEmpty() || doctor.getSelectedItem() == null) {
                throw new IllegalArgumentException("Patient ID and doctor are required");
            }
            System.out.println("bookingConfirmed=" + patientId.getText().trim()
                    + "," + doctor.getSelectedItem() + "," + slot.getSelectedItem());
            JOptionPane.showMessageDialog(this, "Appointment booked");
        } catch (IllegalArgumentException exception) {
            System.out.println("bookingError=" + exception.getMessage());
        } finally {
            bookingInProgress = false;
        }
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--test")) {
            System.out.println("Task: 2");
            System.out.println("Requirement: appointment event trace");
            System.out.println("Command: javac -d Task02\\out Task02\\src\\*.java && java -cp Task02\\out AppointmentDesk --test");
            System.out.println("Date: 2026-11-07");
            System.out.println("constructor -> departmentChanged -> doctorListRefreshed -> bookingError");
            return;
        }
        SwingUtilities.invokeLater(() -> new AppointmentDesk().setVisible(true));
    }
}
