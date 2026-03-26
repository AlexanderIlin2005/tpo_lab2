package ru.sashil.system;

import ru.sashil.functions.AbstractFunction;
import ru.sashil.functions.trigonometric.*;
import ru.sashil.functions.logarithmic.*;

public class EquationSystem extends AbstractFunction {

    @Override
    public double calculate(double x) {
        if (x < 0) {
            return calculateTrigonometricPart(x);
        } else if (x > 0) {
            if (Math.abs(x - 1.0) < 1e-12) {
                throw new ArithmeticException("Функция не определена в точке x = 1");
            }
            return calculateLogarithmicPart(x);
        } else {
            throw new ArithmeticException("Функция не определена в точке x = 0");
        }
    }

    private static double calculateTrigonometricPart(double x) {
        double sec = SecFunction.sec(x);
        double cot = CotFunction.cot(x);
        double sin = SinFunction.sin(x);
        double csc = CscFunction.csc(x);

        double secSq = sec * sec;
        double secSqMulCot = secSq * cot;
        double secSqMulCotCubed = Math.pow(secSqMulCot, 3);
        double firstPart = secSqMulCotCubed + cot;

        double cscSq = csc * csc;
        double sinDivCscSq = sin / cscSq;
        double secondPart = sinDivCscSq * sinDivCscSq;

        return firstPart - secondPart;
    }

    private static double calculateLogarithmicPart(double x) {
        double log3 = Log3Function.log3(x);
        double log5 = Log5Function.log5(x);
        double log10 = Log10Function.log10(x);
        double ln = LnFunction.ln(x);

        double ratio = log3 / log10;
        double ratioCubed = ratio * ratio * ratio;
        double firstInner = ratioCubed * log5;
        double firstPart = firstInner * firstInner;

        double denominator = log10 - log3;
        if (Math.abs(denominator) < 1e-12) {
            throw new ArithmeticException("Деление на ноль в логарифмической части");
        }
        double secondInner = (log3 / denominator) - (log10 * ln);
        double secondPart = secondInner / log10;

        return firstPart - secondPart;
    }
}
