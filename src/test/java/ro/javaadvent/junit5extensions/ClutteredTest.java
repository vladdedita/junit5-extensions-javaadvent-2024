package ro.javaadvent.junit5extensions;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClutteredTest {
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @BeforeAll
    static void setUp() throws IOException, InterruptedException {
        postgres.start();
        postgres.execInContainer("psql", "-U", "testuser", "-d", "testdb", "-c", "CREATE TABLE public.user (id SERIAL PRIMARY KEY, name TEXT)");
        postgres.execInContainer("psql", "-U", "testuser", "-d", "testdb", "-c", "INSERT INTO public.user (name) VALUES ('Alice'), ('Bob')");
    }

    @AfterAll
    static void tearDown() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    EntityManager entityManager;

    @Test
    @Transactional
    void testUserCreation() {
        // Query data
        var result = entityManager.createNativeQuery("SELECT COUNT(*) FROM public.user").getSingleResult();
        assertThat(result).isEqualTo(2L);
    }
}