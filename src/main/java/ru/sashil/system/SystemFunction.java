package ru.sashil.system;

import ru.sashil.functions.trigonometric.*;
import ru.sashil.functions.logarithmic.*;

public class SystemFunction {

    public static double calculate(double x) {
        if (x < 0) {
            return calculateTrigonometricPart(x);
        } else if (x > 0) {
            return calculateLogarithmicPart(x);
        } else {
            // x = 0 - точка разрыва, не определена
            throw new ArithmeticException("Функция не определена в точке x = 0");
        }
    }

    private static double calculateTrigonometricPart(double x) {
        // (((((sec(x)^2) * cot(x))^3) + cot(x)) - ((sin(x) / (csc(x)^2))^2))
        try {
            double sec = SecFunction.sec(x);
            double cot = CotFunction.cot(x);
            double sin = SinFunction.sin(x);
            double csc = CscFunction.csc(x);

            double secSq = sec * sec;
            double secSqMulCot = secSq * cot;
            double secSqMulCotCubed = Math.pow(secSqMulCot, 3);
            double firstPart = secSqMulCotCubed + cot;

            double sinDivCscSq = sin / (csc * csc);
            double secondPart = Math.pow(sinDivCscSq, 2);

            return firstPart - secondPart;
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Функция не определена в точке x = " + x);
        }
    }

    private static double calculateLogarithmicPart(double x) {
        // (((((log_3(x) / log_10(x))^3) * log_5(x))^2) - (((log_3(x) / (log_10(x) - log_3(x))) - (log_10(x) * ln(x))) / log_10(x)))
        try {
            double log3 = Log3Function.log3(x);
            double log5 = Log5Function.log5(x);
            double log10 = Log10Function.log10(x);
            double ln = LnFunction.ln(x);

            double ratio = log3 / log10;
            double ratioCubed = Math.pow(ratio, 3);
            double firstInner = ratioCubed * log5;
            double firstPart = Math.pow(firstInner, 2);

            double denominator = log10 - log3;
            if (Math.abs(denominator) < 1e-12) {
                throw new ArithmeticException("Деление на ноль в логарифмической части");
            }
            double secondInner = (log3 / denominator) - (log10 * ln);
            double secondPart = secondInner / log10;

            return firstPart - secondPart;
        } catch (IllegalArgumentException | ArithmeticException e) {
            throw new ArithmeticException("Функция не определена в точке x = " + x);
        }
    }
}
