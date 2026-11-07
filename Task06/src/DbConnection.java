import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DbConnection {
    private final Properties properties;

    public DbConnection(Path propertiesPath) throws IOException {
        properties = new Properties();
        try (InputStream input = Files.newInputStream(propertiesPath)) {
            properties.load(input);
        }
    }

    public Connection open() throws SQLException {
        String url = required("db.url");
        String user = required("db.user");
        String password = required("db.password");
        return DriverManager.getConnection(url, user, password);
    }

    private String required(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing property: " + key);
        }
        return value;
    }
}
