package ru.sashil.functions.logarithmic;

public class LnFunction {
    private static final double EPSILON = 1e-12;

    public static double ln(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("ln(x) определен только для x > 0");
        }

        if (x == 1.0) return 0.0;

        // Используем ln(x) = -ln(1/x) для x < 1
        if (x < 0.5) {
            return -ln(1.0 / x);
        }

        // Используем ln(x) = ln(2) + ln(x/2) для больших x
        if (x > 2) {
            return ln(x / 2) + 0.6931471805599453;
        }

        // Теперь x в [0.5, 2], используем разложение для ln(1 + y)
        double y = x - 1;
        double result = 0.0;
        double term = y;
        int n = 1;

        while (Math.abs(term) > EPSILON) {
            result += term;
            term *= -y * n / (n + 1);
            n++;
        }
        return result;
    }
}
