import java.util.List;
import java.util.Map;

public final class AppointmentService {
    private final Map<String, List<String>> doctorsByDepartment = Map.of(
            "Cardiology", List.of("Dr. Meera", "Dr. Arun"),
            "Neurology", List.of("Dr. Priya", "Dr. Kiran"),
            "General Medicine", List.of("Dr. Ravi", "Dr. Anitha"));

    public List<String> doctorsFor(String department) {
        return doctorsByDepartment.getOrDefault(department, List.of());
    }

    public Appointment validateAndBook(
            String patientId, String patientName, String department, String doctor, String timeSlot) {
        if (patientId == null || patientId.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient ID is required.");
        }
        if (patientName == null || patientName.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name is required.");
        }
        if (department == null || doctor == null || timeSlot == null
                || department.isBlank() || doctor.isBlank() || timeSlot.isBlank()) {
            throw new IllegalArgumentException("Select department, doctor, and time slot.");
        }
        if (!doctorsFor(department).contains(doctor)) {
            throw new IllegalArgumentException("Selected doctor is not in this department.");
        }
        return new Appointment(patientId.trim(), patientName.trim(), department, doctor, timeSlot);
    }
}
