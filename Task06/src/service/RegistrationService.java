package service;

import dao.RegistrationDao;
import model.Event;
import model.Registration;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public final class RegistrationService {
    private final RegistrationDao dao;
    public RegistrationService(RegistrationDao dao) { this.dao = dao; }
    public void register(String id, String name, String email, String department, int eventId)
            throws SQLException {
        String cleanId = required(id, "Student ID");
        String cleanName = required(name, "Name");
        String cleanEmail = required(email, "Email");
        String cleanDepartment = required(department, "Department");
        if (!cleanEmail.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("Invalid email");
        }
        if (dao.exists(cleanId, eventId)) throw new IllegalArgumentException("Duplicate registration");
        try {
            dao.insert(cleanId, cleanName, cleanEmail, cleanDepartment, eventId);
        } catch (SQLIntegrityConstraintViolationException exception) {
            throw new IllegalArgumentException("Duplicate registration", exception);
        }
    }
    public List<Event> events() throws SQLException { return dao.events(); }
    public List<Registration> page(Integer eventId, int limit, int offset) throws SQLException {
        return dao.page(eventId, limit, offset);
    }
    private String required(String value, String field) {
        if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException(field + " required");
        return value.trim();
    }
}
