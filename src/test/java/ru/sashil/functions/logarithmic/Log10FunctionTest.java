package ru.sashil.functions.logarithmic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class Log10FunctionTest {

    private static final double EPSILON = 1e-6;

    @ParameterizedTest
    @CsvSource({
        "1.0, 0.0",
        "10.0, 1.0",
        "100.0, 2.0"
    })
    void testKnownValues(double x, double expected) {
        assertEquals(expected, Log10Function.log10(x), EPSILON);
    }

    @Test
    void testUndefinedAtZero() {
        assertThrows(IllegalArgumentException.class, () -> Log10Function.log10(0.0));
    }

    @Test
    void testUndefinedAtNegative() {
        assertThrows(IllegalArgumentException.class, () -> Log10Function.log10(-1.0));
        assertThrows(IllegalArgumentException.class, () -> Log10Function.log10(-0.5));
        assertThrows(IllegalArgumentException.class, () -> Log10Function.log10(-10.0));
    }
}
