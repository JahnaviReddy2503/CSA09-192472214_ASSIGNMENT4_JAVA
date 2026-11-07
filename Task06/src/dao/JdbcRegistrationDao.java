package dao;

import model.Event;
import model.Registration;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class JdbcRegistrationDao implements RegistrationDao {
    private final DbConnection db;
    public JdbcRegistrationDao(DbConnection db) { this.db = db; }
    @Override public List<Event> events() throws SQLException {
        List<Event> values = new ArrayList<>();
        try (Connection c = db.open(); PreparedStatement s = c.prepareStatement(
                "SELECT event_id,event_name,event_date,venue FROM events ORDER BY event_date");
             ResultSet r = s.executeQuery()) {
            while (r.next()) values.add(new Event(r.getInt(1), r.getString(2),
                    r.getString(3), r.getString(4)));
        }
        return values;
    }
    @Override public boolean exists(String studentId, int eventId) throws SQLException {
        try (Connection c = db.open(); PreparedStatement s = c.prepareStatement(
                "SELECT 1 FROM registrations WHERE student_id=? AND event_id=?")) {
            s.setString(1, studentId); s.setInt(2, eventId);
            try (ResultSet r = s.executeQuery()) { return r.next(); }
        }
    }
    @Override public void insert(String id, String name, String email, String department, int eventId)
            throws SQLException {
        try (Connection c = db.open()) {
            c.setAutoCommit(false);
            try (PreparedStatement student = c.prepareStatement(
                    "INSERT INTO students(student_id,name,email,department) VALUES(?,?,?,?) "
                            + "ON DUPLICATE KEY UPDATE name=?,email=?,department=?");
                 PreparedStatement registration = c.prepareStatement(
                         "INSERT INTO registrations(student_id,event_id) VALUES(?,?)")) {
                for (int i = 1; i <= 4; i++) student.setString(i, new String[]{id,name,email,department}[i-1]);
                student.setString(5, name); student.setString(6, email); student.setString(7, department);
                student.executeUpdate(); registration.setString(1, id); registration.setInt(2, eventId);
                registration.executeUpdate(); c.commit();
            } catch (SQLException e) { c.rollback(); throw e; }
        }
    }
    @Override public List<Registration> page(Integer eventId, int limit, int offset) throws SQLException {
        String filter = eventId == null ? "" : " WHERE r.event_id=?";
        String sql = "SELECT r.registration_id,s.student_id,s.name,s.email,s.department,"
                + "e.event_name,r.registered_at FROM registrations r JOIN students s ON "
                + "s.student_id=r.student_id JOIN events e ON e.event_id=r.event_id" + filter
                + " ORDER BY r.registration_id LIMIT ? OFFSET ?";
        List<Registration> values = new ArrayList<>();
        try (Connection c = db.open(); PreparedStatement s = c.prepareStatement(sql)) {
            int p = 1; if (eventId != null) s.setInt(p++, eventId);
            s.setInt(p++, limit); s.setInt(p, offset);
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) values.add(new Registration(r.getInt(1), r.getString(2),
                        r.getString(3), r.getString(4), r.getString(5), r.getString(6), r.getString(7)));
            }
        }
        return values;
    }
}
