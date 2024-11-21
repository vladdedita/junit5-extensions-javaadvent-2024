package ro.javaadvent.junit5extensions;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

class ClutteredTest {
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest").withDatabaseName("testdb").withUsername("user").withPassword("password");

    @BeforeAll
    static void setUp() {
        postgres.start();
        System.setProperty("DB_URL", postgres.getJdbcUrl());
        System.setProperty("DB_USERNAME", postgres.getUsername());
        System.setProperty("DB_PASSWORD", postgres.getPassword());
    }

    @AfterAll
    static void tearDown() {
        postgres.stop();
    }

    @Test
    void test() {
        // Test logic
    }
}