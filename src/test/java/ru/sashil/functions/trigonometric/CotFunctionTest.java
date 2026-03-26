package ru.sashil.functions.trigonometric;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
    void testDiscontinuity() {
        assertThrows(ArithmeticException.class, () -> {
            CotFunction.cot(0.0);
        });
    }

    @Test
    void testPeriodicity() {
        double x = 1.0;
        double result1 = CotFunction.cot(x);
        double result2 = CotFunction.cot(x + Math.PI);
        assertEquals(result1, result2, 1e-4);
    }
}
