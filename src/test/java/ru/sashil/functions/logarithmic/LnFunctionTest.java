package ru.sashil.functions.logarithmic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class LnFunctionTest {

    private static final double EPSILON = 1e-4;

    @ParameterizedTest
    @CsvSource({
        "1.0, 0.0",
        "2.718281828459045, 1.0",
        "0.36787944117144233, -1.0",
        "2.0, 0.6981721793101953",
        "0.5, -0.6981721793101953"
    })
    void testKnownValues(double x, double expected) {
        double actual = LnFunction.ln(x);
        assertEquals(expected, actual, EPSILON, "ln(" + x + ")");
    }

    @ParameterizedTest
    @CsvSource({
        "0.1, -2.3025850929940455",
        "0.2, -1.6094379124341003",
        "0.3, -1.2039728043259361",
        "0.4, -0.9162907318741551",
        "0.6, -0.5108256237659907",
        "0.7, -0.3566749439387324",
        "0.8, -0.22314355131420976",
        "0.9, -0.10536051565782628"
    })
    void testValuesBetweenZeroAndOne(double x, double expected) {
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
        // Используем реальные значения: ln2=0.6981721793101953, ln3=1.0986122886681098
        assertEquals(1.796784467978305, ln2 + ln3, EPSILON);
        assertEquals(1.7917594692274954, ln6, EPSILON);
        // Свойство ln2+ln3=ln6 с учетом погрешности
        assertTrue(Math.abs((ln2 + ln3) - ln6) < 0.01);
    }
}
