import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class RegistrationFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 10;
    private final RegistrationService service;
    private final JTextField studentIdField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField departmentField = new JTextField();
    private final JComboBox<EventOption> eventBox = new JComboBox<>();
    private final JComboBox<EventFilter> filterBox = new JComboBox<>();
    private final RegistrationTableModel tableModel = new RegistrationTableModel();
    private final JTable registrationTable = new JTable(tableModel);
    private final JButton previousButton = new JButton("Previous");
    private final JButton nextButton = new JButton("Next");
    private final JLabel pageLabel = new JLabel("Page 1");
    private final JLabel statusLabel = new JLabel("Loading...");
    private int page;

    public RegistrationFrame(RegistrationService service) {
        this.service = service;
        setTitle("Campus Event Registration");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1100, 620);
        setLocationRelativeTo(null);
        buildUi();
        attachListeners();
    }

    private void buildUi() {
        setJMenuBar(createMenuBar());
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        root.add(createFormPanel(), BorderLayout.NORTH);
        root.add(new JScrollPane(registrationTable), BorderLayout.CENTER);
        root.add(createNavigationPanel(), BorderLayout.SOUTH);
        add(root);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 4, 6, 6));
        panel.add(new JLabel("Student ID")); panel.add(studentIdField);
        panel.add(new JLabel("Name")); panel.add(nameField);
        panel.add(new JLabel("Email")); panel.add(emailField);
        panel.add(new JLabel("Phone")); panel.add(phoneField);
        panel.add(new JLabel("Department")); panel.add(departmentField);
        panel.add(new JLabel("Event")); panel.add(eventBox);
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(event -> register());
        panel.add(registerButton);
        panel.add(new JLabel("Filter event")); panel.add(filterBox);
        filterBox.addActionListener(event -> reloadFromFirstPage());
        return panel;
    }

    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        previousButton.addActionListener(event -> loadPage(page - 1));
        nextButton.addActionListener(event -> loadPage(page + 1));
        panel.add(previousButton); panel.add(nextButton); panel.add(pageLabel);
        panel.add(statusLabel);
        return panel;
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem export = new JMenuItem("Export");
        JMenuItem exit = new JMenuItem("Exit");
        export.addActionListener(event -> exportCsv());
        exit.addActionListener(event -> dispose());
        file.add(export); file.add(exit);
        JMenu view = new JMenu("View");
        JMenuItem refresh = new JMenuItem("Refresh");
        refresh.addActionListener(event -> reloadFromFirstPage());
        view.add(refresh);
        bar.add(file); bar.add(view);
        return bar;
    }

    private void attachListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                dispose();
            }
        });
    }

    private void register() {
        EventOption selected = (EventOption) eventBox.getSelectedItem();
        if (selected == null) {
            showError("Select an event.");
            return;
        }
        setBusy(true);
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                service.register(studentIdField.getText(), nameField.getText(),
                        emailField.getText(), phoneField.getText(),
                        departmentField.getText(), selected.id());
                return null;
            }
            @Override protected void done() {
                try {
                    get();
                    statusLabel.setText("Registration saved.");
                    clearForm();
                    loadPage(0);
                } catch (Exception exception) {
                    showError(userMessage(exception));
                    setBusy(false);
                }
            }
        }.execute();
    }

    private void reloadFromFirstPage() {
        loadPage(0);
    }

    private void loadPage(int requestedPage) {
        if (requestedPage < 0) return;
        page = requestedPage;
        setBusy(true);
        EventFilter selected = (EventFilter) filterBox.getSelectedItem();
        Integer eventId = selected == null ? null : selected.id();
        new SwingWorker<List<RegistrationRow>, Void>() {
            @Override protected List<RegistrationRow> doInBackground() throws Exception {
                return service.findPage(eventId, PAGE_SIZE, page * PAGE_SIZE);
            }
            @Override protected void done() {
                try {
                    List<RegistrationRow> rows = get();
                    tableModel.setRows(rows);
                    pageLabel.setText("Page " + (page + 1));
                    previousButton.setEnabled(page > 0);
                    nextButton.setEnabled(rows.size() == PAGE_SIZE);
                    statusLabel.setText(rows.size() + " row(s) loaded.");
                } catch (Exception exception) {
                    showError(userMessage(exception));
                } finally {
                    setBusy(false);
                }
            }
        }.execute();
    }

    private void loadEvents() {
        new SwingWorker<List<EventOption>, Void>() {
            @Override protected List<EventOption> doInBackground() throws Exception {
                return service.findEvents();
            }
            @Override protected void done() {
                try {
                    List<EventOption> events = get();
                    eventBox.setModel(new DefaultComboBoxModel<>(events.toArray(EventOption[]::new)));
                    List<EventFilter> filters = new ArrayList<>();
                    filters.add(new EventFilter(null, "All events"));
                    events.forEach(event -> filters.add(new EventFilter(event.id(), event.name())));
                    filterBox.setModel(new DefaultComboBoxModel<>(filters.toArray(EventFilter[]::new)));
                    loadPage(0);
                } catch (Exception exception) {
                    showError(userMessage(exception));
                    setBusy(false);
                }
            }
        }.execute();
    }

    private void exportCsv() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        Path path = chooser.getSelectedFile().toPath();
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("registration_id,student_id,name,email,phone,event,registered_at\n");
            for (RegistrationRow row : tableModel.getRows()) {
                writer.write(csv(row.registrationId()) + "," + csv(row.studentId()) + ","
                        + csv(row.studentName()) + "," + csv(row.email()) + ","
                        + csv(row.phone()) + "," + csv(row.eventName()) + ","
                        + csv(row.registeredAt()) + "\n");
            }
            statusLabel.setText("Exported " + tableModel.getRowCount() + " row(s).");
        } catch (IOException exception) {
            showError("Export failed: " + exception.getMessage());
        }
    }

    private String csv(Object value) {
        return "\"" + String.valueOf(value).replace("\"", "\"\"") + "\"";
    }

    private void clearForm() {
        studentIdField.setText(""); nameField.setText("");
        emailField.setText(""); phoneField.setText("");
        departmentField.setText("");
    }

    private void setBusy(boolean busy) {
        previousButton.setEnabled(!busy && page > 0);
        nextButton.setEnabled(!busy);
    }

    private void showError(String message) {
        statusLabel.setText(message);
        JOptionPane.showMessageDialog(this, message, "Registration", JOptionPane.ERROR_MESSAGE);
    }

    private String userMessage(Exception exception) {
        Throwable cause = exception.getCause() == null ? exception : exception.getCause();
        if (cause instanceof IllegalArgumentException || cause instanceof SQLException) {
            return cause.getMessage();
        }
        return "Operation failed: " + cause.getMessage();
    }

    public void start() {
        setVisible(true);
        loadEvents();
    }

    private record EventFilter(Integer id, String name) {
        @Override public String toString() { return name; }
    }
}
