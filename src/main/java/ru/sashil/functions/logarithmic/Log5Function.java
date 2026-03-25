package ru.sashil.functions.logarithmic;

public class Log5Function {
    private static final double LN5 = 1.6094379124341003; // ln(5)

    public static double log5(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("log_5(x) определен только для x > 0");
        }
        return LnFunction.ln(x) / LN5;
    }
}
