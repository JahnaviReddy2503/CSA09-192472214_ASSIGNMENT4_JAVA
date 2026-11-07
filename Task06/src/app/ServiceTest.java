package app;
import dao.MockRegistrationDao;
import service.RegistrationService;
public final class ServiceTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Task: 6");
        System.out.println("Requirement: mock service normal, invalid, duplicate cases");
        System.out.println("Command: javac -d Task06\\out Task06\\src\\*.java && java -cp Task06\\out app.ServiceTest");
        System.out.println("Date: 2026-11-07");
        RegistrationService service = new RegistrationService(new MockRegistrationDao());
        try { service.register("ST101", "Asha", "asha@example.com", "Computer Science", 1); System.out.println("normal: PASS"); }
        catch (Exception e) { System.out.println("normal: FAIL " + e.getMessage()); }
        try { service.register("ST102", "", "bad", "Computer Science", 1); System.out.println("invalid: FAIL"); }
        catch (IllegalArgumentException e) { System.out.println("invalid: PASS - " + e.getMessage()); }
        try { service.register("ST101", "Asha", "asha@example.com", "Computer Science", 1); System.out.println("duplicate: FAIL"); }
        catch (IllegalArgumentException e) { System.out.println("duplicate: PASS - " + e.getMessage()); }
        System.out.println("MySQL: NOT EXECUTED – MySQL server and Connector/J are unavailable in this environment.");
    }
}
