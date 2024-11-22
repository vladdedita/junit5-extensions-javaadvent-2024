package ro.javaadvent.junit5extensions.metrics;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import ro.javaadvent.junit5extensions.extensions.PostgreSQLExtension;
import ro.javaadvent.junit5extensions.extensions.ReportingExtension;
import ro.javaadvent.junit5extensions.extensions.SQLiteExtension;
import ro.javaadvent.junit5extensions.extensions.models.ReportingState;

@ExtendWith(ReportingExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        // We need to enable lazy initialization to avoid the EntityManager creation during the context loading
        "spring.main.lazy-initialization=true"
})
public class MetricsTests {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Nested
    @ExtendWith(PostgreSQLExtension.class)
    class PostgresMetricsTests {

        @Test
        void testPostgresMetrics(ReportingState reportingState) {
            var metrics = testRestTemplate.getForObject("/api/metrics", String.class);
            reportingState.addResult("postgres", metrics);
        }
    }

    @Nested
    @ExtendWith(SQLiteExtension.class)
    class SqliteMetricsTests {

        @Test
        void testSqliteMetrics(ReportingState reportingState) {
            var metrics = testRestTemplate.getForObject("/api/metrics", String.class);
            reportingState.addResult("sqlite", metrics);
        }
    }
}
