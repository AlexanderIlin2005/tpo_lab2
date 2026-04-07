package ru.sashil.functions.trigonometric;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class SinFunctionTest {

    private static final double EPSILON = 1e-6;

    @ParameterizedTest
    @CsvSource({
        "0.0, 0.0",
        "1.57079632679, 1.0",
        "3.14159265359, 0.0",
        "4.71238898038, -1.0",
        "6.28318530718, 0.0"
    })
    void testKnownValues(double x, double expected) {
        assertEquals(expected, SinFunction.sin(x), EPSILON);
    }

    @Test
    void testPeriodicity() {
        double x = 1.0;
        double result1 = SinFunction.sin(x);
        double result2 = SinFunction.sin(x + 2 * Math.PI);
        double result3 = SinFunction.sin(x + 4 * Math.PI);
        assertEquals(result1, result2, EPSILON);
        assertEquals(result1, result3, EPSILON);
    }

    @Test
    void testLargeAngles() {
        // Проверка нормализации больших углов
        double x = 100.0;
        double expected = SinFunction.sin(x % (2 * Math.PI));
        double actual = SinFunction.sin(x);
        assertEquals(expected, actual, EPSILON, "sin(100) должен нормализоваться");
    }

    @Test
    void testVeryLargeAngles() {
        double x = 1000.0;
        assertDoesNotThrow(() -> SinFunction.sin(x));
    }

    @Test
    void testNegativeLargeAngles() {
        double x = -100.0;
        assertDoesNotThrow(() -> SinFunction.sin(x));
    }

    @Test
    void testOddFunction() {
        double x = 0.5;
        assertEquals(SinFunction.sin(-x), -SinFunction.sin(x), EPSILON);
    }

    @Test
    void testVerySmallAngle() {
        double x = 1e-10;
        double result = SinFunction.sin(x);
        assertEquals(x, result, 1e-9);
    }
}
