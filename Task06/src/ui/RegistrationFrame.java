package ui;

import dao.RegistrationDao;
import model.Event;
import model.Registration;
import service.RegistrationService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class RegistrationFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private final RegistrationService service;
    private final JTextField id = new JTextField();
    private final JTextField name = new JTextField();
    private final JTextField email = new JTextField();
    private final JTextField department = new JTextField();
    private final JComboBox<Event> events = new JComboBox<>();
    private final JComboBox<Event> filter = new JComboBox<>();
    private final DefaultTableModel table = new DefaultTableModel(
            new String[]{"ID","Student","Name","Email","Department","Event","Date"}, 0);
    private final JLabel status = new JLabel("Ready");
    private int page;
    public RegistrationFrame(RegistrationService service) {
        this.service = service; setTitle("Campus Event Registration");
        setSize(950, 560); setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel form = new JPanel(new GridLayout(3, 5, 5, 5));
        form.add(new JLabel("Student ID")); form.add(id); form.add(new JLabel("Name")); form.add(name);
        form.add(new JLabel("Email")); form.add(email); form.add(new JLabel("Department")); form.add(department);
        form.add(new JLabel("Event")); form.add(events);
        JButton register = new JButton("Register"); JButton refresh = new JButton("Refresh");
        form.add(register); form.add(refresh); form.add(new JLabel("Filter")); form.add(filter);
        add(form, BorderLayout.NORTH); add(new JScrollPane(new JTable(table)), BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
        JMenuBar bar = new JMenuBar(); JMenu file = new JMenu("File");
        JMenuItem export = new JMenuItem("Export"); JMenuItem exit = new JMenuItem("Exit");
        JMenu view = new JMenu("View"); JMenuItem reload = new JMenuItem("Refresh");
        JMenu help = new JMenu("Help"); JMenuItem about = new JMenuItem("About");
        export.addActionListener(e -> exportCsv()); exit.addActionListener(e -> dispose());
        reload.addActionListener(e -> loadPage()); refresh.addActionListener(e -> loadPage());
        about.addActionListener(e -> JOptionPane.showMessageDialog(this, "CSA09 Event Registration"));
        file.add(export); file.add(exit); view.add(reload); help.add(about);
        bar.add(file); bar.add(view); bar.add(help); setJMenuBar(bar);
        register.addActionListener(e -> register()); filter.addActionListener(e -> loadPage());
        loadEvents();
    }
    private void loadEvents() {
        new SwingWorker<List<Event>,Void>() {
            protected List<Event> doInBackground() throws Exception { return service.events(); }
            protected void done() { try { List<Event> values = get();
                values.forEach(events::addItem); filter.addItem(null); values.forEach(filter::addItem); loadPage();
            } catch (Exception e) { status.setText("Database error: " + e.getMessage()); } }
        }.execute();
    }
    private void register() {
        Event event = (Event) events.getSelectedItem(); if (event == null) return;
        new SwingWorker<Void,Void>() {
            protected Void doInBackground() throws Exception {
                service.register(id.getText(), name.getText(), email.getText(), department.getText(), event.id()); return null;
            }
            protected void done() { try { get(); status.setText("Registration successful"); loadPage(); }
            catch (Exception e) { status.setText(e.getCause() == null ? e.getMessage() : e.getCause().getMessage()); } }
        }.execute();
    }
    private void loadPage() {
        Event event = (Event) filter.getSelectedItem(); Integer eventId = event == null ? null : event.id();
        int currentPage = page;
        new SwingWorker<List<Registration>,Void>() {
            protected List<Registration> doInBackground() throws Exception { return service.page(eventId, 20, currentPage * 20); }
            protected void done() { try { table.setRowCount(0); for (Registration r : get())
                table.addRow(new Object[]{r.id(),r.studentId(),r.name(),r.email(),r.department(),r.eventName(),r.registeredAt()});
                status.setText("Page " + (currentPage + 1) + " loaded"); } catch (Exception e) { status.setText(e.getMessage()); } }
        }.execute();
    }
    private void exportCsv() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        Path target = chooser.getSelectedFile().toPath();
        try (BufferedWriter writer = Files.newBufferedWriter(target)) {
            for (int column = 0; column < table.getColumnCount(); column++) {
                if (column > 0) writer.write(",");
                writer.write(table.getColumnName(column));
            }
            writer.newLine();
            for (int row = 0; row < table.getRowCount(); row++) {
                for (int column = 0; column < table.getColumnCount(); column++) {
                    if (column > 0) writer.write(",");
                    writer.write(csv(table.getValueAt(row, column)));
                }
                writer.newLine();
            }
            status.setText("CSV exported: " + target);
        } catch (IOException exception) {
            status.setText("CSV export failed: " + exception.getMessage());
        }
    }

    private String csv(Object value) {
        return "\"" + String.valueOf(value).replace("\"", "\"\"") + "\"";
    }
}
