package ro.javaadvent.junit5extensions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ro.javaadvent.junit5extensions.extensions.PostgreSQLExtension;

@ExtendWith(PostgreSQLExtension.class)
class CleanPostgreSQLTest {
    @Test
    void test() {
        // Test logic
    }
}