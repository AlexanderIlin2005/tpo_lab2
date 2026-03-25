package ru.sashil.functions.logarithmic;

public class Log3Function {
    private static final double LN3 = 1.0986122886681098; // ln(3)

    public static double log3(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("log_3(x) определен только для x > 0");
        }
        return LnFunction.ln(x) / LN3;
    }
}
