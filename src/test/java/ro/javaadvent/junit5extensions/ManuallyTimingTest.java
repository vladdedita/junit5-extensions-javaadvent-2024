package ro.javaadvent.junit5extensions;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

class ManuallyTimingTest {

    private long startTime;

    @BeforeEach
    void startTimer() {
        startTime = System.currentTimeMillis();
    }

    @AfterEach
    void logDuration(TestInfo testInfo) {
        long duration = System.currentTimeMillis() - startTime;
        System.out.println(testInfo.getDisplayName() + " took " + duration + " ms");
    }

    @Test
    void test() {
        // Test logic
    }

}
