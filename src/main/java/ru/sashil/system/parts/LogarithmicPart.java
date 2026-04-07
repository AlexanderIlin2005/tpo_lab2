package ru.sashil.system.parts;

import ru.sashil.functions.logarithmic.*;

public class LogarithmicPart {

    public static double calculate(double x) {
        // Формула: (((((log_3(x) / log_10(x))^3) * log_5(x))^2) - (((log_3(x) / (log_10(x) - log_3(x))) - (log_10(x) * ln(x))) / log_10(x))
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
            throw new ArithmeticException("Деление на ноль в логарифмической части: знаменатель = 0");
        }
        double secondInner = (log3 / denominator) - (log10 * ln);
        double secondPart = secondInner / log10;

        return firstPart - secondPart;
    }
}
