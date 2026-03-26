package ru.sashil.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import ru.sashil.system.SystemFunction;
import ru.sashil.util.CSVLoader;
import ru.sashil.functions.trigonometric.*;
import ru.sashil.functions.logarithmic.*;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IntegrationWithStubsTest {

    private static SystemFunction system;
    private static Map<Double, Double> sinExpected;
    private static Map<Double, Double> cosExpected;
    private static Map<Double, Double> secExpected;
    private static Map<Double, Double> cscExpected;
    private static Map<Double, Double> cotExpected;
    private static Map<Double, Double> lnExpected;
    private static Map<Double, Double> log3Expected;
    private static Map<Double, Double> log5Expected;
    private static Map<Double, Double> log10Expected;
    private static Map<Double, Double> systemExpected;

    private static final double EPSILON = 1e-6;

    @BeforeAll
    static void setUp() throws IOException {
        system = new SystemFunction();


        sinExpected = CSVLoader.loadFunctionValues("src/test/resources/expected/sin_expected.csv");
        cosExpected = CSVLoader.loadFunctionValues("src/test/resources/expected/cos_expected.csv");
        secExpected = CSVLoader.loadFunctionValues("src/test/resources/expected/sec_expected.csv");
        cscExpected = CSVLoader.loadFunctionValues("src/test/resources/expected/csc_expected.csv");
        cotExpected = CSVLoader.loadFunctionValues("src/test/resources/expected/cot_expected.csv");
        lnExpected = CSVLoader.loadFunctionValues("src/test/resources/expected/ln_expected.csv");
        log3Expected = CSVLoader.loadFunctionValues("src/test/resources/expected/log3_expected.csv");
        log5Expected = CSVLoader.loadFunctionValues("src/test/resources/expected/log5_expected.csv");
        log10Expected = CSVLoader.loadFunctionValues("src/test/resources/expected/log10_expected.csv");
        systemExpected = CSVLoader.loadFunctionValues("src/test/resources/expected/system_expected.csv");
    }


    @ParameterizedTest
    @CsvSource({
        "-1.5, -0.997494986604054",
        "-1.0, -0.8414709848078965",
        "-0.5, -0.479425538604203",
        "0.5, 0.479425538604203",
        "1.0, 0.8414709848078965"
    })
    void testSinAgainstExpected(double x, double expected) {
        double computed = SinFunction.sin(x);
        assertEquals(expected, computed, EPSILON);
    }


    @Test
    void testTrigonometricPartWithExpectedStubs() {
        double x = -1.0;


        double secStub = CSVLoader.interpolate(secExpected, x);
        double cotStub = CSVLoader.interpolate(cotExpected, x);
        double sinStub = CSVLoader.interpolate(sinExpected, x);
        double cscStub = CSVLoader.interpolate(cscExpected, x);

        try (MockedStatic<SecFunction> secMock = mockStatic(SecFunction.class);
             MockedStatic<CotFunction> cotMock = mockStatic(CotFunction.class);
             MockedStatic<SinFunction> sinMock = mockStatic(SinFunction.class);
             MockedStatic<CscFunction> cscMock = mockStatic(CscFunction.class)) {

            secMock.when(() -> SecFunction.sec(x)).thenReturn(secStub);
            cotMock.when(() -> CotFunction.cot(x)).thenReturn(cotStub);
            sinMock.when(() -> SinFunction.sin(x)).thenReturn(sinStub);
            cscMock.when(() -> CscFunction.csc(x)).thenReturn(cscStub);

            double result = system.calculate(x);


            secMock.verify(() -> SecFunction.sec(x), times(1));
            cotMock.verify(() -> CotFunction.cot(x), times(1));
            sinMock.verify(() -> SinFunction.sin(x), times(1));
            cscMock.verify(() -> CscFunction.csc(x), times(1));


            double expected = CSVLoader.interpolate(systemExpected, x);
            assertEquals(expected, result, 0.5);
        }
    }


    @Test
    void testLogarithmicPartWithExpectedStubs() {
        double x = 2.0;

        double log3Stub = CSVLoader.interpolate(log3Expected, x);
        double log5Stub = CSVLoader.interpolate(log5Expected, x);
        double log10Stub = CSVLoader.interpolate(log10Expected, x);
        double lnStub = CSVLoader.interpolate(lnExpected, x);

        try (MockedStatic<Log3Function> log3Mock = mockStatic(Log3Function.class);
             MockedStatic<Log5Function> log5Mock = mockStatic(Log5Function.class);
             MockedStatic<Log10Function> log10Mock = mockStatic(Log10Function.class);
             MockedStatic<LnFunction> lnMock = mockStatic(LnFunction.class)) {

            log3Mock.when(() -> Log3Function.log3(x)).thenReturn(log3Stub);
            log5Mock.when(() -> Log5Function.log5(x)).thenReturn(log5Stub);
            log10Mock.when(() -> Log10Function.log10(x)).thenReturn(log10Stub);
            lnMock.when(() -> LnFunction.ln(x)).thenReturn(lnStub);

            double result = system.calculate(x);

            log3Mock.verify(() -> Log3Function.log3(x), times(1));
            log5Mock.verify(() -> Log5Function.log5(x), times(1));
            log10Mock.verify(() -> Log10Function.log10(x), times(1));
            lnMock.verify(() -> LnFunction.ln(x), times(1));

            double expected = CSVLoader.interpolate(systemExpected, x);
            assertEquals(expected, result, 0.5);
        }
    }

    @Test
    void testSystemFunctionAtZero() {
        assertThrows(ArithmeticException.class, () -> system.calculate(0.0));
    }

    @Test
    void testSystemFunctionAtOne() {
        assertThrows(ArithmeticException.class, () -> system.calculate(1.0));
    }
}
