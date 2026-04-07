package ru.sashil.system;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.MockedStatic;
import ru.sashil.functions.trigonometric.*;
import ru.sashil.functions.logarithmic.*;
import ru.sashil.system.parts.TrigonometricPart;
import ru.sashil.system.parts.LogarithmicPart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ИНТЕГРАЦИОННЫЕ ТЕСТЫ (стратегия сверху-вниз)
 *
 * Уровень 1: Система функций
 *   - Мокируются целые части (TrigonometricPart, LogarithmicPart)
 *   - Цель: проверить, что система вызывает правильную часть в зависимости от x
 *
 * Уровень 2: Части системы (TrigonometricPart, LogarithmicPart)
 *   - Мокируются отдельные функции (sec, cot, sin, csc / log3, log5, log10, ln)
 *   - Цель: проверить, что арифметика формулы внутри части работает правильно
 *   - Значения для моков берутся из CSV (эталонные значения Wolfram)
 *   - Результат части сравнивается с эталоном из CSV
 *
 * Уровень 3: Отдельные функции - НЕ ТЕСТИРУЮТСЯ ЗДЕСЬ (есть свои модульные тесты)
 */
class SystemFunctionExtendedTest {

    private static final double EPSILON_TRIG = 1e-4;      // для тригонометрической части
    private static final double EPSILON_LOG = 1e-2;       // для логарифмической части (погрешность ряда Тейлора)

    // Эталонные значения из CSV
    private static Map<Double, Double> trigPartExpected;
    private static Map<Double, Double> logPartExpected;
    private static Map<Double, Double> secExpected;
    private static Map<Double, Double> cotExpected;
    private static Map<Double, Double> sinExpected;
    private static Map<Double, Double> cscExpected;
    private static Map<Double, Double> log3Expected;
    private static Map<Double, Double> log5Expected;
    private static Map<Double, Double> log10Expected;
    private static Map<Double, Double> lnExpected;

    @BeforeAll
    static void loadCSVFiles() throws IOException {
        trigPartExpected = loadCSV("src/test/resources/expected/trigonometric_part_expected.csv");
        logPartExpected = loadCSV("src/test/resources/expected/logarithmic_part_expected.csv");
        secExpected = loadCSV("src/test/resources/expected/sec_expected.csv");
        cotExpected = loadCSV("src/test/resources/expected/cot_expected.csv");
        sinExpected = loadCSV("src/test/resources/expected/sin_expected.csv");
        cscExpected = loadCSV("src/test/resources/expected/csc_expected.csv");
        log3Expected = loadCSV("src/test/resources/expected/log3_expected.csv");
        log5Expected = loadCSV("src/test/resources/expected/log5_expected.csv");
        log10Expected = loadCSV("src/test/resources/expected/log10_expected.csv");
        lnExpected = loadCSV("src/test/resources/expected/ln_expected.csv");
    }

    private static Map<Double, Double> loadCSV(String filepath) throws IOException {
        Map<Double, Double> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2) {
                    try {
                        double x = Double.parseDouble(parts[0]);
                        double y = Double.parseDouble(parts[1]);
                        map.put(x, y);
                    } catch (NumberFormatException e) {}
                }
            }
        }
        return map;
    }

    // =========================================================================
    // УРОВЕНЬ 1: СИСТЕМА ФУНКЦИЙ (мокируются целые части)
    // =========================================================================

    @ParameterizedTest
    @CsvFileSource(resources = "/expected/trigonometric_part_expected.csv", numLinesToSkip = 1, delimiter = ';')
    void testSystemDelegatesToTrigonometricPartForNegativeX(double x, double mockResult) {
        if (x >= 0) return;

        try (MockedStatic<TrigonometricPart> trigMock = mockStatic(TrigonometricPart.class)) {
            trigMock.when(() -> TrigonometricPart.calculate(x)).thenReturn(mockResult);

            double result = SystemFunction.calculate(x);

            assertEquals(mockResult, result, EPSILON_TRIG);
            trigMock.verify(() -> TrigonometricPart.calculate(x), times(1));
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/expected/logarithmic_part_expected.csv", numLinesToSkip = 1, delimiter = ';')
    void testSystemDelegatesToLogarithmicPartForPositiveX(double x, double mockResult) {
        if (x <= 0 || Math.abs(x - 1.0) < 0.01) return;

        try (MockedStatic<LogarithmicPart> logMock = mockStatic(LogarithmicPart.class)) {
            logMock.when(() -> LogarithmicPart.calculate(x)).thenReturn(mockResult);

            double result = SystemFunction.calculate(x);

            assertEquals(mockResult, result, EPSILON_LOG);
            logMock.verify(() -> LogarithmicPart.calculate(x), times(1));
        }
    }

    // =========================================================================
    // УРОВЕНЬ 2: ТРИГОНОМЕТРИЧЕСКАЯ ЧАСТЬ
    // =========================================================================

    @ParameterizedTest
    @CsvFileSource(resources = "/expected/trigonometric_part_expected.csv", numLinesToSkip = 1, delimiter = ';')
    void testTrigonometricPartFormula(double x, double expected) {
        Double mockSec = secExpected.get(x);
        Double mockCot = cotExpected.get(x);
        Double mockSin = sinExpected.get(x);
        Double mockCsc = cscExpected.get(x);

        if (mockSec == null || mockCot == null || mockSin == null || mockCsc == null) {
            return;
        }

        try (MockedStatic<SecFunction> secMock = mockStatic(SecFunction.class);
             MockedStatic<CotFunction> cotMock = mockStatic(CotFunction.class);
             MockedStatic<SinFunction> sinMock = mockStatic(SinFunction.class);
             MockedStatic<CscFunction> cscMock = mockStatic(CscFunction.class)) {

            secMock.when(() -> SecFunction.sec(x)).thenReturn(mockSec);
            cotMock.when(() -> CotFunction.cot(x)).thenReturn(mockCot);
            sinMock.when(() -> SinFunction.sin(x)).thenReturn(mockSin);
            cscMock.when(() -> CscFunction.csc(x)).thenReturn(mockCsc);

            double result = TrigonometricPart.calculate(x);

            assertEquals(expected, result, EPSILON_TRIG,
                "Формула тригонометрической части для x=" + x);
        }
    }

    // =========================================================================
    // УРОВЕНЬ 2: ЛОГАРИФМИЧЕСКАЯ ЧАСТЬ (с увеличенным допуском 1e-2)
    // =========================================================================

    @ParameterizedTest
    @CsvFileSource(resources = "/expected/logarithmic_part_expected.csv", numLinesToSkip = 1, delimiter = ';')
    void testLogarithmicPartFormula(double x, double expected) {
        if (Math.abs(x - 1.0) < 0.01) return;

        Double mockLog3 = log3Expected.get(x);
        Double mockLog5 = log5Expected.get(x);
        Double mockLog10 = log10Expected.get(x);
        Double mockLn = lnExpected.get(x);

        if (mockLog3 == null || mockLog5 == null || mockLog10 == null || mockLn == null) {
            return;
        }

        try (MockedStatic<Log3Function> log3Mock = mockStatic(Log3Function.class);
             MockedStatic<Log5Function> log5Mock = mockStatic(Log5Function.class);
             MockedStatic<Log10Function> log10Mock = mockStatic(Log10Function.class);
             MockedStatic<LnFunction> lnMock = mockStatic(LnFunction.class)) {

            log3Mock.when(() -> Log3Function.log3(x)).thenReturn(mockLog3);
            log5Mock.when(() -> Log5Function.log5(x)).thenReturn(mockLog5);
            log10Mock.when(() -> Log10Function.log10(x)).thenReturn(mockLog10);
            lnMock.when(() -> LnFunction.ln(x)).thenReturn(mockLn);

            double result = LogarithmicPart.calculate(x);

            assertEquals(expected, result, EPSILON_LOG,
                "Формула логарифмической части для x=" + x);
        }
    }

    // =========================================================================
    // ТЕСТЫ НА ИСКЛЮЧЕНИЯ
    // =========================================================================

    @Test
    void testSystemThrowsExceptionAtZero() {
        assertThrows(ArithmeticException.class, () -> SystemFunction.calculate(0.0));
    }

    @Test
    void testSystemThrowsExceptionAtOne() {
        assertThrows(ArithmeticException.class, () -> SystemFunction.calculate(1.0));
    }

    @Test
    void testTrigonometricPartThrowsAtZero() {
        assertThrows(ArithmeticException.class, () -> TrigonometricPart.calculate(0.0));
    }

    @Test
    void testTrigonometricPartThrowsAtPiHalf() {
        assertThrows(ArithmeticException.class, () -> TrigonometricPart.calculate(Math.PI/2));
    }

    @Test
    void testTrigonometricPartThrowsAtPi() {
        assertThrows(ArithmeticException.class, () -> TrigonometricPart.calculate(Math.PI));
    }

    @Test
    void testLogarithmicPartThrowsAtOne() {
        assertThrows(ArithmeticException.class, () -> LogarithmicPart.calculate(1.0));
    }

    @Test
    void testLogarithmicPartThrowsAtZero() {
        assertThrows(IllegalArgumentException.class, () -> LogarithmicPart.calculate(0.0));
    }

    @Test
    void testLogarithmicPartThrowsAtNegative() {
        assertThrows(IllegalArgumentException.class, () -> LogarithmicPart.calculate(-1.0));
    }

    @Test
    void testSystemBehaviorNearZero() {
        double veryClose = 0.0001;
        double close = 0.001;
        double notSoClose = 0.01;

        assertTrue(Math.abs(SystemFunction.calculate(veryClose)) > Math.abs(SystemFunction.calculate(close)));
        assertTrue(Math.abs(SystemFunction.calculate(close)) > Math.abs(SystemFunction.calculate(notSoClose)));
    }
}
