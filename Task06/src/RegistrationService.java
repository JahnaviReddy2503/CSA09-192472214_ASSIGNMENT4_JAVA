import java.sql.SQLException;
import java.util.List;

public final class RegistrationService {
    private final RegistrationDao registrationDao;

    public RegistrationService(RegistrationDao registrationDao) {
        this.registrationDao = registrationDao;
    }

    public void register(String studentId, String name, String email, String phone,
                         String department, int eventId)
            throws SQLException {
        String cleanStudentId = required(studentId, "Student ID");
        String cleanName = required(name, "Name");
        String cleanEmail = required(email, "Email");
        String cleanPhone = required(phone, "Phone");
        String cleanDepartment = required(department, "Department");
        if (!cleanEmail.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("Enter a valid email.");
        }
        String digits = cleanPhone.replaceAll("[\\s()-]", "");
        if (!digits.matches("\\d{7,15}")) {
            throw new IllegalArgumentException("Phone must contain 7 to 15 digits.");
        }
        if (registrationDao.registrationExists(cleanStudentId, eventId)) {
            throw new IllegalArgumentException("This student is already registered for the event.");
        }
        registrationDao.register(cleanStudentId, cleanName, cleanEmail, cleanPhone,
                cleanDepartment, eventId);
    }

    public List<EventOption> findEvents() throws SQLException {
        return registrationDao.findEvents();
    }

    public List<RegistrationRow> findPage(Integer eventId, int limit, int offset)
            throws SQLException {
        return registrationDao.findPage(eventId, limit, offset);
    }

    private String required(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " is required.");
        }
        return value.trim();
    }
}
