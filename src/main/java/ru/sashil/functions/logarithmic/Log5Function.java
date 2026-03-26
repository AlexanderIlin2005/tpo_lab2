package ru.sashil.functions.logarithmic;

import ru.sashil.functions.AbstractFunction;

public class Log5Function extends AbstractFunction {
    private static final double LN5 = 1.6094379124341003;

    @Override
    public double calculate(double x) {
        return log5(x);
    }

    public static double log5(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("log_5(x) определен только для x > 0");
        }
        return LnFunction.ln(x) / LN5;
    }
}
