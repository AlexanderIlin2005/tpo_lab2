package ru.sashil.functions.logarithmic;

public class Log10Function {
    private static final double LN10 = 2.302585092994046; // ln(10)

    public static double log10(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("log_10(x) определен только для x > 0");
        }
        return LnFunction.ln(x) / LN10;
    }
}
