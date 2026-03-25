package ru.sashil.functions.trigonometric;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class SecFunctionTest {

    private static final double EPSILON = 1e-4;

    @ParameterizedTest
    @CsvSource({
        "0.0, 1.0",
        "1.0471975512, 2.0",
        "2.09439510239, -2.0"
    })
    void testKnownValues(double x, double expected) {
        assertEquals(expected, SecFunction.sec(x), EPSILON);
    }

    @Test
    void testDiscontinuity() {
        assertThrows(ArithmeticException.class, () -> {
            SecFunction.sec(Math.PI / 2);
        });
    }

    @Test
    void testEvenFunction() {
        double x = 0.5;
        assertEquals(SecFunction.sec(-x), SecFunction.sec(x), EPSILON);
    }

    @Test
    void testPeriodicity() {
        double x = 1.0;
        double result1 = SecFunction.sec(x);
        double result2 = SecFunction.sec(x + 2 * Math.PI);
        assertEquals(result1, result2, 1e-4);
    }
}
