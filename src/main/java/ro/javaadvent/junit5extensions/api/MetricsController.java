package ro.javaadvent.junit5extensions.api;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricsController {

    record Statistics(double averageTime, double meanTime, double maxTime, double minTime) {
    }

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping
    public Statistics getMetrics() {
        //some db work
        entityManager.createNativeQuery("SELECT count(*) from user").getSingleResult();
        return new Statistics(
                Math.random(),
                Math.random(),
                Math.random(),
                Math.random()
        );
    }
}
