package ro.javaadvent.junit5extensions.extensions;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class ReportingExtension implements TestWatcher, BeforeEachCallback {
    private long startTime;

    @Override
    public void testDisabled(ExtensionContext context, java.util.Optional<String> reason) {
        System.out.println(context.getDisplayName() + " DISABLED" + reason.map(r -> ": " + r).orElse(""));
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        long duration = System.currentTimeMillis() - startTime;
        System.out.println(context.getDisplayName() + " PASSED in " + duration + " ms");
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        long duration = System.currentTimeMillis() - startTime;
        System.out.println(context.getDisplayName() + " FAILED in " + duration + " ms with " + cause.getMessage());
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        System.out.println(context.getDisplayName() + " ABORTED with " + cause.getMessage());
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        startTime = System.currentTimeMillis();
    }
}