import java.nio.file.Path;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) throws Exception {
        DbConnection connection = new DbConnection(Path.of("Task06", "db.properties"));
        RegistrationService service = new RegistrationService(new RegistrationDao(connection));
        javax.swing.SwingUtilities.invokeLater(() -> new RegistrationFrame(service).start());
    }
}
