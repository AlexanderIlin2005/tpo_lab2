package ru.sashil.functions.logarithmic;

import ru.sashil.functions.AbstractFunction;

public class Log10Function extends AbstractFunction {
    private static final double LN10 = 2.302585092994046;

    @Override
    public double calculate(double x) {
        return log10(x);
    }

    public static double log10(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("log_10(x) определен только для x > 0");
        }
        return LnFunction.ln(x) / LN10;
    }
}
