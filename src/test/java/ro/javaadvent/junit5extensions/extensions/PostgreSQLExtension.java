package ro.javaadvent.junit5extensions.extensions;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreSQLExtension implements BeforeAllCallback, AfterAllCallback {
    private PostgreSQLContainer<?> postgres;

    @Override
    public void beforeAll(ExtensionContext context) {
        postgres = new PostgreSQLContainer<>("postgres:latest").withDatabaseName("testdb").withUsername("user").withPassword("password");
        postgres.start();
        System.setProperty("DB_URL", postgres.getJdbcUrl());
        System.setProperty("DB_USERNAME", postgres.getUsername());
        System.setProperty("DB_PASSWORD", postgres.getPassword());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (postgres != null) {
            postgres.stop();
        }
    }
}