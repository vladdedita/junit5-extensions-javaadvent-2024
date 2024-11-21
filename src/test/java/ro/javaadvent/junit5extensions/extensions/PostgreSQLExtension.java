package ro.javaadvent.junit5extensions.extensions;

import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;

public class PostgreSQLExtension implements BeforeAllCallback, AfterAllCallback {

    private static PostgreSQLContainer<?> postgres;

    @Override
    @SneakyThrows
    public void beforeAll(ExtensionContext context) {
        // Start the PostgreSQL container
        postgres = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass");
        postgres.start();
        initProperties();
        initData();
    }

    /**
     * We would normally use @DynamicPropertySource, but this doesn't work in the case of extensions
     * as the Spring Context is initialized before the extension is called, resulting in the properties not being set.
     */
    private static void initProperties() {
        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
        System.setProperty("spring.datasource.driver-class-name", "org.postgresql.Driver");
        System.setProperty("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQLDialect");
    }

    private static void initData() throws IOException, InterruptedException {
        postgres.execInContainer("psql", "-U", "testuser", "-d", "testdb", "-c", "CREATE TABLE public.user (id SERIAL PRIMARY KEY, name TEXT)");
        postgres.execInContainer("psql", "-U", "testuser", "-d", "testdb", "-c", "INSERT INTO public.user (name) VALUES ('Alice'), ('Bob')");
    }

    @Override
    public void afterAll(ExtensionContext context) {
        // Stop the PostgreSQL container
        if (postgres != null) {
            postgres.stop();
        }
    }
}