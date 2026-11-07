package dao;

import model.Event;
import model.Registration;
import java.sql.SQLException;
import java.util.List;

public interface RegistrationDao {
    List<Event> events() throws SQLException;
    boolean exists(String studentId, int eventId) throws SQLException;
    void insert(String studentId, String name, String email, String department, int eventId)
            throws SQLException;
    List<Registration> page(Integer eventId, int limit, int offset) throws SQLException;
}
