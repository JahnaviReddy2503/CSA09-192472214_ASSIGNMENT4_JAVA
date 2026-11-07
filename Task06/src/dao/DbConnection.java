package dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DbConnection {
    private final Properties properties = new Properties();
    public DbConnection(Path path) throws IOException {
        try (InputStream input = Files.newInputStream(path)) { properties.load(input); }
    }
    public Connection open() throws SQLException {
        return DriverManager.getConnection(properties.getProperty("db.url"),
                properties.getProperty("db.user"), properties.getProperty("db.password"));
    }
}
