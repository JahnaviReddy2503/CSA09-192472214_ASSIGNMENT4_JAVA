import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class RegistrationDao {
    private final DbConnection dbConnection;

    public RegistrationDao(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<EventOption> findEvents() throws SQLException {
        String sql = "SELECT event_id, event_name, event_date, venue FROM events ORDER BY event_date, event_name";
        List<EventOption> events = new ArrayList<>();
        try (Connection connection = dbConnection.open();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {
            while (results.next()) {
                events.add(new EventOption(results.getInt("event_id"),
                        results.getString("event_name"),
                        results.getString("event_date"),
                        results.getString("venue")));
            }
        }
        return events;
    }

    public boolean registrationExists(String studentId, int eventId) throws SQLException {
        String sql = "SELECT 1 FROM registrations WHERE student_id = ? AND event_id = ?";
        try (Connection connection = dbConnection.open();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, studentId);
            statement.setInt(2, eventId);
            try (ResultSet results = statement.executeQuery()) {
                return results.next();
            }
        }
    }

    public void register(String studentId, String name, String email, String phone,
                         String department, int eventId)
            throws SQLException {
        String studentSql = """
                INSERT INTO students (student_id, name, email, phone, department) VALUES (?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE name = ?, email = ?, phone = ?, department = ?
                """;
        String registrationSql =
                "INSERT INTO registrations (student_id, event_id) VALUES (?, ?)";
        try (Connection connection = dbConnection.open()) {
            connection.setAutoCommit(false);
            try (PreparedStatement student = connection.prepareStatement(studentSql);
                 PreparedStatement registration = connection.prepareStatement(registrationSql)) {
                student.setString(1, studentId);
                student.setString(2, name);
                student.setString(3, email);
                student.setString(4, phone);
                student.setString(5, department);
                student.setString(6, name);
                student.setString(7, email);
                student.setString(8, phone);
                student.setString(9, department);
                student.executeUpdate();
                registration.setString(1, studentId);
                registration.setInt(2, eventId);
                registration.executeUpdate();
                connection.commit();
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            }
        }
    }

    public List<RegistrationRow> findPage(Integer eventId, int limit, int offset)
            throws SQLException {
        String filter = eventId == null ? "" : " WHERE r.event_id = ?";
        String sql = """
                SELECT r.registration_id, s.student_id, s.name, s.email, s.phone,
                       s.department, e.event_name, r.registered_at
                FROM registrations r
                JOIN students s ON s.student_id = r.student_id
                JOIN events e ON e.event_id = r.event_id
                %s
                ORDER BY r.registration_id
                LIMIT ? OFFSET ?
                """.formatted(filter);
        List<RegistrationRow> rows = new ArrayList<>();
        try (Connection connection = dbConnection.open();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            int parameter = 1;
            if (eventId != null) {
                statement.setInt(parameter++, eventId);
            }
            statement.setInt(parameter++, limit);
            statement.setInt(parameter, offset);
            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    rows.add(new RegistrationRow(
                            results.getInt("registration_id"),
                            results.getString("student_id"),
                            results.getString("name"),
                            results.getString("email"),
                            results.getString("phone"),
                            results.getString("department"),
                            results.getString("event_name"),
                            results.getString("registered_at")));
                }
            }
        }
        return rows;
    }
}
