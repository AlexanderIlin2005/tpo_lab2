package ru.sashil.functions.logarithmic;

import ru.sashil.functions.AbstractFunction;

public class Log3Function extends AbstractFunction {
    private static final double LN3 = 1.0986122886681098;

    @Override
    public double calculate(double x) {
        return log3(x);
    }

    public static double log3(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("log_3(x) определен только для x > 0");
        }
        return LnFunction.ln(x) / LN3;
    }
}
