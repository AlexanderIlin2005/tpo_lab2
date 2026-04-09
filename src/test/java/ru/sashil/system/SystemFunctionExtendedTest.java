package ru.sashil.system;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
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

class SystemFunctionExtendedTest {

    private static final double EPSILON_TRIG = 1e-4;
    private static final double EPSILON_LOG = 1e-2;

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
            String line = reader.readLine();
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


    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -2.0, -0.5, -10.0})
    void testSystemDoesNotCallLogarithmicPartForNegativeX(double x) {
        try (MockedStatic<LogarithmicPart> logMock = mockStatic(LogarithmicPart.class)) {
            SystemFunction.calculate(x);
            logMock.verifyNoInteractions();
        }
    }




    @ParameterizedTest
    @ValueSource(doubles = {0.5, 1.5, 2.0, 5.0, 10.0})
    void testSystemDoesNotCallTrigonometricPartForPositiveX(double x) {
        if (Math.abs(x - 1.0) < 0.01) return;

        try (MockedStatic<TrigonometricPart> trigMock = mockStatic(TrigonometricPart.class)) {
            SystemFunction.calculate(x);
            trigMock.verifyNoInteractions();
        }
    }


    @ParameterizedTest
    @ValueSource(doubles = {-Math.PI/2, -Math.PI, -2*Math.PI, -3*Math.PI/2})
    void testSystemThrowsAtTrigonometricDiscontinuityPoints(double x) {
        assertThrows(ArithmeticException.class, () -> SystemFunction.calculate(x),
            "Система должна выбрасывать исключение в точке разрыва x=" + x);
    }



    @ParameterizedTest
    @CsvFileSource(resources = "/expected/trigonometric_part_expected.csv", numLinesToSkip = 1, delimiter = ';')
    void testTrigonometricPartFormula(double x, double expected) {
        if (x >= 0) return;

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
            assertEquals(expected, result, EPSILON_TRIG, "x=" + x);
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/expected/logarithmic_part_expected.csv", numLinesToSkip = 1, delimiter = ';')
    void testLogarithmicPartFormula(double x, double expected) {
        if (x <= 0 || Math.abs(x - 1.0) < 0.01) return;

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
            assertEquals(expected, result, EPSILON_LOG, "x=" + x);
        }
    }



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
    void testSystemGrowsAsXApproachesZeroFromPositive() {
        double closer = 0.0001;
        double further = 0.001;
        assertTrue(Math.abs(SystemFunction.calculate(closer)) > Math.abs(SystemFunction.calculate(further)));
    }

    @Test
    void testSystemGrowsAsXApproachesZeroFromNegative() {
        double closer = -0.0001;
        double further = -0.001;
        assertTrue(Math.abs(SystemFunction.calculate(closer)) > Math.abs(SystemFunction.calculate(further)));
    }
}
