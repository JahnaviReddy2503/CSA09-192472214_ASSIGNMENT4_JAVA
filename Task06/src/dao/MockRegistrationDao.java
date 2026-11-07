package dao;

import model.Event;
import model.Registration;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class MockRegistrationDao implements RegistrationDao {
    private final List<Event> events = new ArrayList<>(List.of(
            new Event(1, "Java Workshop", "2026-12-01", "Seminar Hall")));
    private final List<Registration> registrations = new ArrayList<>();

    @Override public List<Event> events() { return List.copyOf(events); }
    @Override public boolean exists(String studentId, int eventId) {
        return registrations.stream().anyMatch(row -> row.studentId().equals(studentId)
                && row.eventName().equals("Java Workshop") && eventId == 1);
    }
    @Override public void insert(String studentId, String name, String email,
                                 String department, int eventId) {
        registrations.add(new Registration(registrations.size() + 1, studentId, name,
                email, department, "Java Workshop", "mock-now"));
    }
    @Override public List<Registration> page(Integer eventId, int limit, int offset) {
        return registrations.stream().skip(offset).limit(limit).toList();
    }
}
