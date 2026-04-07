package ru.sashil.system;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class SystemFunctionExtendedTest {

    private static final double EPSILON = 1e-4;

    // ==================== ТЕСТЫ НА НЕПРАВИЛЬНЫЕ СЦЕНАРИИ ====================

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -2.0, -0.5, -10.0})
    void testLogarithmicPartWithNegativeX(double x) {
        assertThrows(IllegalArgumentException.class, () -> ru.sashil.system.parts.LogarithmicPart.calculate(x));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.1, 0.5, 0.8, 1.0, 1.2, 1.4})
    void testTrigonometricPartWithPositiveX(double x) {
        assertDoesNotThrow(() -> ru.sashil.system.parts.TrigonometricPart.calculate(x));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Math.PI/2, Math.PI, 3*Math.PI/2, 2*Math.PI})
    void testTrigonometricPartWithPositiveXDiscontinuity(double x) {
        assertThrows(ArithmeticException.class, () -> ru.sashil.system.parts.TrigonometricPart.calculate(x));
    }

    @Test
    void testTrigonometricPartAtZero() {
        assertThrows(ArithmeticException.class, () -> ru.sashil.system.parts.TrigonometricPart.calculate(0.0));
    }

    @Test
    void testLogarithmicPartAtZero() {
        assertThrows(IllegalArgumentException.class, () -> ru.sashil.system.parts.LogarithmicPart.calculate(0.0));
    }

    @Test
    void testDivisionByZeroInSec() {
        assertThrows(ArithmeticException.class, () -> ru.sashil.functions.trigonometric.SecFunction.sec(Math.PI/2));
    }

    @Test
    void testDivisionByZeroInCsc() {
        assertThrows(ArithmeticException.class, () -> ru.sashil.functions.trigonometric.CscFunction.csc(0.0));
    }

    @Test
    void testDivisionByZeroInCot() {
        assertThrows(ArithmeticException.class, () -> ru.sashil.functions.trigonometric.CotFunction.cot(0.0));
    }

    @Test
    void testDivisionByZeroInLogarithmicPart() {
        assertThrows(ArithmeticException.class, () -> ru.sashil.system.parts.LogarithmicPart.calculate(1.0));
    }

    @Test
    void testSystemNearZero() {
        double veryClose = 0.0001;
        double close = 0.001;
        double notSoClose = 0.01;

        assertTrue(Math.abs(SystemFunction.calculate(veryClose)) > Math.abs(SystemFunction.calculate(close)));
        assertTrue(Math.abs(SystemFunction.calculate(close)) > Math.abs(SystemFunction.calculate(notSoClose)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, Math.PI, 2*Math.PI, Math.PI/2, 3*Math.PI/2})
    void testTrigonometricPartUndefinedPoints(double x) {
        assertThrows(ArithmeticException.class, () -> ru.sashil.system.parts.TrigonometricPart.calculate(x));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.1, -1.0, -10.0})
    void testLogarithmicPartUndefinedForNegative(double x) {
        assertThrows(IllegalArgumentException.class, () -> ru.sashil.system.parts.LogarithmicPart.calculate(x));
    }

    // ==================== ПРЯМОЕ СРАВНЕНИЕ С CSV ====================

    @ParameterizedTest
    @CsvFileSource(resources = "/expected/trigonometric_part_expected.csv", numLinesToSkip = 1, delimiter = ';')
    void testTrigonometricPartAgainstCSV(double x, double expected) {
        double actual = ru.sashil.system.parts.TrigonometricPart.calculate(x);
        assertEquals(expected, actual, EPSILON, "x=" + x);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/expected/logarithmic_part_expected.csv", numLinesToSkip = 1, delimiter = ';')
    void testLogarithmicPartAgainstCSV(double x, double expected) {
        if (Math.abs(x - 1.0) < 0.01) return;
        double actual = ru.sashil.system.parts.LogarithmicPart.calculate(x);
        assertEquals(expected, actual, EPSILON, "x=" + x);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/expected/sin_expected.csv", numLinesToSkip = 1, delimiter = ';')
    void testSinAgainstCSV(double x, double expected) {
        double actual = ru.sashil.functions.trigonometric.SinFunction.sin(x);
        assertEquals(expected, actual, EPSILON, "sin(" + x + ")");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/expected/cos_expected.csv", numLinesToSkip = 1, delimiter = ';')
    void testCosAgainstCSV(double x, double expected) {
        double actual = ru.sashil.functions.trigonometric.CosFunction.cos(x);
        assertEquals(expected, actual, EPSILON, "cos(" + x + ")");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/expected/sec_expected.csv", numLinesToSkip = 1, delimiter = ';')
    void testSecAgainstCSV(double x, double expected) {
        double actual = ru.sashil.functions.trigonometric.SecFunction.sec(x);
        assertEquals(expected, actual, EPSILON, "sec(" + x + ")");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/expected/csc_expected.csv", numLinesToSkip = 1, delimiter = ';')
    void testCscAgainstCSV(double x, double expected) {
        double actual = ru.sashil.functions.trigonometric.CscFunction.csc(x);
        assertEquals(expected, actual, EPSILON, "csc(" + x + ")");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/expected/cot_expected.csv", numLinesToSkip = 1, delimiter = ';')
    void testCotAgainstCSV(double x, double expected) {
        double actual = ru.sashil.functions.trigonometric.CotFunction.cot(x);
        assertEquals(expected, actual, EPSILON, "cot(" + x + ")");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/expected/ln_expected.csv", numLinesToSkip = 1, delimiter = ';')
    void testLnAgainstCSV(double x, double expected) {
        if (x <= 0) return;
        double actual = ru.sashil.functions.logarithmic.LnFunction.ln(x);
        assertEquals(expected, actual, EPSILON, "ln(" + x + ")");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/expected/log3_expected.csv", numLinesToSkip = 1, delimiter = ';')
    void testLog3AgainstCSV(double x, double expected) {
        if (x <= 0) return;
        double actual = ru.sashil.functions.logarithmic.Log3Function.log3(x);
        assertEquals(expected, actual, EPSILON, "log3(" + x + ")");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/expected/log5_expected.csv", numLinesToSkip = 1, delimiter = ';')
    void testLog5AgainstCSV(double x, double expected) {
        if (x <= 0) return;
        double actual = ru.sashil.functions.logarithmic.Log5Function.log5(x);
        assertEquals(expected, actual, EPSILON, "log5(" + x + ")");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/expected/log10_expected.csv", numLinesToSkip = 1, delimiter = ';')
    void testLog10AgainstCSV(double x, double expected) {
        if (x <= 0) return;
        double actual = ru.sashil.functions.logarithmic.Log10Function.log10(x);
        assertEquals(expected, actual, EPSILON, "log10(" + x + ")");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/expected/system_expected.csv", numLinesToSkip = 1, delimiter = ';')
    void testSystemAgainstCSV(double x, double expected) {
        if (Math.abs(x) < 1e-12 || Math.abs(x - 1.0) < 1e-12) return;
        double actual = SystemFunction.calculate(x);
        assertEquals(expected, actual, EPSILON, "system(" + x + ")");
    }

    // ==================== СТАРЫЕ ТЕСТЫ ДЛЯ СОВМЕСТИМОСТИ ====================

    @Test
    void testZeroPoint() {
        assertThrows(ArithmeticException.class, () -> SystemFunction.calculate(0.0));
    }

    @Test
    void testOnePoint() {
        assertThrows(ArithmeticException.class, () -> SystemFunction.calculate(1.0));
    }

    @Test
    void testTrigonometricDiscontinuity() {
        assertThrows(ArithmeticException.class, () -> SystemFunction.calculate(-Math.PI));
        assertThrows(ArithmeticException.class, () -> SystemFunction.calculate(-Math.PI / 2));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-2.0, -1.5, -1.0, -0.5, -0.1})
    void testTrigonometricPartNoException(double x) {
        assertDoesNotThrow(() -> SystemFunction.calculate(x));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.5, 2.0, 5.0, 10.0})
    void testLogarithmicPartNoException(double x) {
        assertDoesNotThrow(() -> SystemFunction.calculate(x));
    }
}
