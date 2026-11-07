import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public final class RegistrationTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private static final String[] COLUMNS =
            {"ID", "Student ID", "Name", "Email", "Phone", "Event", "Registered At"};
    private List<RegistrationRow> rows = new ArrayList<>();

    public void setRows(List<RegistrationRow> rows) {
        this.rows = new ArrayList<>(rows);
        fireTableDataChanged();
    }

    public RegistrationRow getRow(int index) {
        return rows.get(index);
    }

    public List<RegistrationRow> getRows() {
        return List.copyOf(rows);
    }

    @Override public int getRowCount() { return rows.size(); }
    @Override public int getColumnCount() { return COLUMNS.length; }
    @Override public String getColumnName(int column) { return COLUMNS[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RegistrationRow row = rows.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> row.registrationId();
            case 1 -> row.studentId();
            case 2 -> row.studentName();
            case 3 -> row.email();
            case 4 -> row.phone();
            case 5 -> row.eventName();
            case 6 -> row.registeredAt();
            default -> "";
        };
    }
}
