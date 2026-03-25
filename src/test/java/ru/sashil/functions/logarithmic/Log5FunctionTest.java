package ru.sashil.functions.logarithmic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class Log5FunctionTest {

    private static final double EPSILON = 1e-6;

    @ParameterizedTest
    @CsvSource({
        "1.0, 0.0",
        "5.0, 1.0",
        "25.0, 2.0"
    })
    void testKnownValues(double x, double expected) {
        assertEquals(expected, Log5Function.log5(x), EPSILON);
    }

    @Test
    void testUndefined() {
        assertThrows(IllegalArgumentException.class, () -> {
            Log5Function.log5(0.0);
        });
    }
}
