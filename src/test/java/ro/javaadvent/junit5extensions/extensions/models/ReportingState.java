package ro.javaadvent.junit5extensions.extensions.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReportingState {
    private final Map<String, String> results = new ConcurrentHashMap<>();

    public void addResult(String testName, String result) {
        results.put(testName, result);
    }

    public void report() {
        System.out.println("\n---- Test Results ----\n");
        results.forEach((testName, result) -> {
            System.out.printf("%s:\n", testName);
            System.out.println(prettyPrintJson(result));
        });
        System.out.println("\n----------------------");
    }

    private String prettyPrintJson(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(jsonString, Object.class);
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            return writer.writeValueAsString(json);
        } catch (Exception e) {
            // If the input is not valid JSON, return the raw string
            return jsonString;
        }


    }

}