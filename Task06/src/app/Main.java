package app;
import dao.DbConnection;
import dao.JdbcRegistrationDao;
import service.RegistrationService;
import ui.RegistrationFrame;
import javax.swing.SwingUtilities;
import java.nio.file.Path;
public final class Main {
    public static void main(String[] args) throws Exception {
        RegistrationService service = new RegistrationService(new JdbcRegistrationDao(
                new DbConnection(Path.of("Task06", "db.properties"))));
        SwingUtilities.invokeLater(() -> new RegistrationFrame(service).setVisible(true));
    }
}
