package ro.javaadvent.junit5extensions.extensions;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.UUID;

public class SQLiteExtension implements BeforeAllCallback, AfterAllCallback {

    static String uniqueDbName = "memdb" + UUID.randomUUID();
    private static final String JDBC_URL = "jdbc:sqlite:file:" + uniqueDbName + "?mode=memory&cache=shared";
    private Connection connection;


    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        // Set system properties
        System.setProperty("spring.datasource.url", JDBC_URL);
        System.setProperty("spring.datasource.driver-class-name", "org.sqlite.JDBC");
        System.setProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");

        // Initialize database schema and data
        connection = DriverManager.getConnection(JDBC_URL);
        initializeDatabase(connection);
    }

    private void initializeDatabase(Connection connection) throws Exception {
        try (Statement stmt = connection.createStatement()) {
            // Create table
            stmt.executeUpdate("CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");

            // Insert data
            stmt.executeUpdate("INSERT INTO user (name) VALUES ('Alice')");
            stmt.executeUpdate("INSERT INTO user (name) VALUES ('Bob')");
        } catch (Exception e) {
            throw new Exception("Failed to initialize database", e);
        }
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}