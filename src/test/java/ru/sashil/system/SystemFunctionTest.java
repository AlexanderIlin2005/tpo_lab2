package ru.sashil.system;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import ru.sashil.functions.trigonometric.*;
import ru.sashil.functions.logarithmic.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SystemFunctionTest {

    private static final double EPSILON = 1e-4;

    @ParameterizedTest
    @CsvSource({
        "-2.0, 0.5",
        "-1.5, 0.2",
        "-1.0, -0.1",
        "-0.5, -0.3",
        "-0.1, -0.5"
    })
    void testTrigonometricPart(double x, double expected) {
        double result = SystemFunction.calculate(x);
        assertEquals(expected, result, EPSILON);
    }

    @ParameterizedTest
    @CsvSource({
        "0.5, -0.2",
        "1.0, -0.5",
        "2.0, -0.8",
        "5.0, -1.2",
        "10.0, -1.5"
    })
    void testLogarithmicPart(double x, double expected) {
        double result = SystemFunction.calculate(x);
        assertEquals(expected, result, EPSILON);
    }

    @Test
    void testZeroPoint() {
        assertThrows(ArithmeticException.class, () -> {
            SystemFunction.calculate(0.0);
        });
    }

    @Test
    void testTrigonometricDiscontinuity() {
        assertThrows(ArithmeticException.class, () -> {
            SystemFunction.calculate(-Math.PI);
        });
        assertThrows(ArithmeticException.class, () -> {
            SystemFunction.calculate(-Math.PI / 2);
        });
    }

    @Test
    void testLogarithmicUndefined() {
        assertThrows(ArithmeticException.class, () -> {
            SystemFunction.calculate(-1.0);
        });
    }

    // Тесты с Mockito заглушками
    @Test
    void testTrigonometricPartWithStubs() {
        try (MockedStatic<SecFunction> secMock = mockStatic(SecFunction.class);
             MockedStatic<CotFunction> cotMock = mockStatic(CotFunction.class);
             MockedStatic<SinFunction> sinMock = mockStatic(SinFunction.class);
             MockedStatic<CscFunction> cscMock = mockStatic(CscFunction.class)) {

            double x = -1.0;
            double expectedResult = -0.5;

            secMock.when(() -> SecFunction.sec(x)).thenReturn(1.8508);
            cotMock.when(() -> CotFunction.cot(x)).thenReturn(-0.6421);
            sinMock.when(() -> SinFunction.sin(x)).thenReturn(-0.8415);
            cscMock.when(() -> CscFunction.csc(x)).thenReturn(-1.1884);

            double result = SystemFunction.calculate(x);

            assertEquals(expectedResult, result, EPSILON);

            secMock.verify(() -> SecFunction.sec(x), times(1));
            cotMock.verify(() -> CotFunction.cot(x), times(1));
            sinMock.verify(() -> SinFunction.sin(x), times(1));
            cscMock.verify(() -> CscFunction.csc(x), times(1));
        }
    }

    @Test
    void testLogarithmicPartWithStubs() {
        try (MockedStatic<Log3Function> log3Mock = mockStatic(Log3Function.class);
             MockedStatic<Log5Function> log5Mock = mockStatic(Log5Function.class);
             MockedStatic<Log10Function> log10Mock = mockStatic(Log10Function.class);
             MockedStatic<LnFunction> lnMock = mockStatic(LnFunction.class)) {

            double x = 2.0;
            double expectedResult = -0.8;

            log3Mock.when(() -> Log3Function.log3(x)).thenReturn(0.6309);
            log5Mock.when(() -> Log5Function.log5(x)).thenReturn(0.4307);
            log10Mock.when(() -> Log10Function.log10(x)).thenReturn(0.3010);
            lnMock.when(() -> LnFunction.ln(x)).thenReturn(0.6931);

            double result = SystemFunction.calculate(x);

            assertEquals(expectedResult, result, EPSILON);

            log3Mock.verify(() -> Log3Function.log3(x), times(1));
            log5Mock.verify(() -> Log5Function.log5(x), times(1));
            log10Mock.verify(() -> Log10Function.log10(x), times(1));
            lnMock.verify(() -> LnFunction.ln(x), times(1));
        }
    }

    @Test
    void testMockitoVerificationWithDifferentX() {
        try (MockedStatic<SecFunction> secMock = mockStatic(SecFunction.class);
             MockedStatic<CotFunction> cotMock = mockStatic(CotFunction.class)) {

            double x1 = -0.5;
            double x2 = -1.0;

            secMock.when(() -> SecFunction.sec(x1)).thenReturn(1.1395);
            secMock.when(() -> SecFunction.sec(x2)).thenReturn(1.8508);
            cotMock.when(() -> CotFunction.cot(x1)).thenReturn(-1.8305);
            cotMock.when(() -> CotFunction.cot(x2)).thenReturn(-0.6421);

            SystemFunction.calculate(x1);
            SystemFunction.calculate(x2);

            secMock.verify(() -> SecFunction.sec(x1), times(1));
            secMock.verify(() -> SecFunction.sec(x2), times(1));
            cotMock.verify(() -> CotFunction.cot(x1), times(1));
            cotMock.verify(() -> CotFunction.cot(x2), times(1));
        }
    }
}
