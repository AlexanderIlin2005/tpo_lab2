package ru.sashil.functions.trigonometric;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
    void testDiscontinuity() {
        assertThrows(ArithmeticException.class, () -> {
            CscFunction.csc(0.0);
        });
    }
}
