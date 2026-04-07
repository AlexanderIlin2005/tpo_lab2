package ru.sashil.functions.trigonometric;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class CosFunctionTest {

    private static final double EPSILON = 1e-6;

    @ParameterizedTest
    @CsvSource({
        "0.0, 1.0",
        "1.57079632679, 0.0",
        "3.14159265359, -1.0",
        "4.71238898038, 0.0",
        "6.28318530718, 1.0"
    })
    void testKnownValues(double x, double expected) {
        assertEquals(expected, CosFunction.cos(x), EPSILON);
    }

    @Test
    void testPeriodicity() {
        double x = 1.0;
        double result1 = CosFunction.cos(x);
        double result2 = CosFunction.cos(x + 2 * Math.PI);
        assertEquals(result1, result2, EPSILON);
    }

    @Test
    void testEvenFunction() {
        double x = 0.5;
        assertEquals(CosFunction.cos(-x), CosFunction.cos(x), EPSILON);
    }
}
