package ro.javaadvent.junit5extensions.extensions;

import org.junit.jupiter.api.extension.*;
import ro.javaadvent.junit5extensions.extensions.models.ReportingState;

import java.util.Optional;

public class ReportingExtension implements TestWatcher, BeforeEachCallback, AfterAllCallback, ParameterResolver {
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ReportingExtension.class);
    private static final String REPORTING_STATE_KEY = "reportingState";

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        // Store the start time for the test
        context.getStore(NAMESPACE).put("startTime", System.currentTimeMillis());
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        long duration = calculateDuration(context);
        ReportingState reportingState = getReportingState(context);
        reportingState.addResult(context.getDisplayName(), "PASSED in " + duration + " ms");
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        long duration = calculateDuration(context);
        ReportingState reportingState = getReportingState(context);
        reportingState.addResult(context.getDisplayName(), "FAILED in " + duration + " ms with " + cause.getMessage());
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        long duration = calculateDuration(context);
        ReportingState reportingState = getReportingState(context);
        reportingState.addResult(context.getDisplayName(), "ABORTED in " + duration + " ms with " + cause.getMessage());
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        ReportingState reportingState = getReportingState(context);
        reportingState.addResult(context.getDisplayName(),
                "DISABLED" + reason.map(r -> ": " + r).orElse(""));
    }

    private long calculateDuration(ExtensionContext context) {
        Long startTime = context.getStore(NAMESPACE).remove("startTime", Long.class);
        return System.currentTimeMillis() - (startTime != null ? startTime : 0);
    }

    private ReportingState getReportingState(ExtensionContext context) {
        // Get the root context's store to ensure a single ReportingState across all tests
        ExtensionContext.Store store = context.getRoot().getStore(NAMESPACE);
        return store.getOrComputeIfAbsent(REPORTING_STATE_KEY, key -> new ReportingState(), ReportingState.class);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        // Retrieve the ReportingState from the root context
        ReportingState reportingState = context.getRoot().getStore(NAMESPACE).get(REPORTING_STATE_KEY, ReportingState.class);
        if (reportingState != null) {
            System.out.println("---- Test Results ----");
            reportingState.report();
        }
    }

    // ParameterResolver methods to inject ReportingState into test methods
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == ReportingState.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getReportingState(extensionContext);
    }
}