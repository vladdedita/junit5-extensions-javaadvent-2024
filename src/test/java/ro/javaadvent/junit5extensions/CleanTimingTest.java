package ro.javaadvent.junit5extensions;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.javaadvent.junit5extensions.extensions.PostgreSQLExtension;
import ro.javaadvent.junit5extensions.extensions.ReportingExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({
        PostgreSQLExtension.class,
        ReportingExtension.class
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        // We need to enable lazy initialization to avoid the EntityManager creation during the context loading
        "spring.main.lazy-initialization=true"
})
class CleanTimingTest {
    @Autowired
    EntityManager entityManager;
    @Test
    void test() {
        // Query data
        var result = entityManager.createNativeQuery("SELECT COUNT(*) FROM public.user").getSingleResult();
        assertThat(result).isEqualTo(2L);
    }
}
