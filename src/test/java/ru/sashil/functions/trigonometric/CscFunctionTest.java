package ru.sashil.functions.trigonometric;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class CscFunctionTest {

    private static final double EPSILON = 1e-4;

    @ParameterizedTest
    @CsvSource({
        "1.57079632679, 1.0",
        "0.5235987756, 2.0",
        "2.61799387799, 2.0"
    })
    void testKnownValues(double x, double expected) {
        assertEquals(expected, CscFunction.csc(x), EPSILON);
    }

    @Test
    void testPeriodicity() {
        double x = 1.0;
        double result1 = CscFunction.csc(x);
        double result2 = CscFunction.csc(x + 2 * Math.PI);
        assertEquals(result1, result2, EPSILON, "csc(x) должна иметь период 2π");
    }

    @Test
    void testDiscontinuityAtZero() {
        assertThrows(ArithmeticException.class, () -> CscFunction.csc(0.0));
    }

    @Test
    void testDiscontinuityAtPi() {
        assertThrows(ArithmeticException.class, () -> CscFunction.csc(Math.PI));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, Math.PI, -Math.PI, 2*Math.PI, -2*Math.PI})
    void testDiscontinuityAtMultiplePoints(double x) {
        assertThrows(ArithmeticException.class, () -> CscFunction.csc(x));
    }
}
