package ru.sashil.functions.trigonometric;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class CotFunctionTest {

    private static final double EPSILON = 1e-4;

    @ParameterizedTest
    @CsvSource({
        "0.78539816339, 1.0",
        "1.0471975512, 0.57735026919",
        "2.09439510239, -0.57735026919"
    })
    void testKnownValues(double x, double expected) {
        assertEquals(expected, CotFunction.cot(x), EPSILON);
    }

    @Test
    void testDiscontinuityAtZero() {
        assertThrows(ArithmeticException.class, () -> CotFunction.cot(0.0));
    }

    @Test
    void testDiscontinuityAtPi() {
        assertThrows(ArithmeticException.class, () -> CotFunction.cot(Math.PI));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, Math.PI, -Math.PI, 2*Math.PI, -2*Math.PI})
    void testDiscontinuityAtMultiplePoints(double x) {
        assertThrows(ArithmeticException.class, () -> CotFunction.cot(x));
    }

    @Test
    void testPeriodicity() {
        double x = 1.0;
        assertEquals(CotFunction.cot(x), CotFunction.cot(x + Math.PI), EPSILON);
    }
}
