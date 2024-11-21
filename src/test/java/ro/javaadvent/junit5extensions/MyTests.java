package ro.javaadvent.junit5extensions;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.javaadvent.junit5extensions.extensions.MyExtension;

@ExtendWith(MyExtension.class)
class MyTests {
    // Test methods
}