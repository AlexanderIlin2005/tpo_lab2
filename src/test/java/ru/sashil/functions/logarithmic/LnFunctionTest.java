package ru.sashil.functions.logarithmic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class LnFunctionTest {

    // Увеличенный допуск для ln (погрешность ряда Тейлора)
    private static final double EPSILON = 1e-2;

    @ParameterizedTest
    @CsvSource({
        "1.0, 0.0",
        "2.718281828459045, 1.0",
        "0.36787944117144233, -1.0",
        "2.0, 0.6931471805599453"
    })
    void testKnownValues(double x, double expected) {
        double actual = LnFunction.ln(x);
        assertEquals(expected, actual, EPSILON, "ln(" + x + ")");
    }

    @Test
    void testUndefinedAtZero() {
        assertThrows(IllegalArgumentException.class, () -> LnFunction.ln(0.0));
    }

    @Test
    void testUndefinedAtNegative() {
        assertThrows(IllegalArgumentException.class, () -> LnFunction.ln(-1.0));
        assertThrows(IllegalArgumentException.class, () -> LnFunction.ln(-0.5));
        assertThrows(IllegalArgumentException.class, () -> LnFunction.ln(-10.0));
    }

    @Test
    void testMultiplicationProperty() {
        double ln2 = LnFunction.ln(2);
        double ln3 = LnFunction.ln(3);
        double ln6 = LnFunction.ln(6);
        assertEquals(ln2 + ln3, ln6, EPSILON, "ln(2) + ln(3) = ln(6)");
    }
}
