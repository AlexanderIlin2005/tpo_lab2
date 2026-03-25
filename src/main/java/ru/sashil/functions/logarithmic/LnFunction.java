package ru.sashil.functions.logarithmic;

public class LnFunction {
    private static final double EPSILON = 1e-12;

    public static double ln(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("ln(x) определен только для x > 0");
        }

        if (Math.abs(x - 1.0) < 1e-12) {
            return 0.0;
        }

        // Для x < 1 используем ln(x) = -ln(1/x)
        if (x < 1.0) {
            return -ln(1.0 / x);
        }

        // Для x > 2 используем ln(x) = ln(2) + ln(x/2)
        if (x > 2.0) {
            return 0.6931471805599453 + ln(x / 2.0);
        }

        // Теперь x в [1, 2], используем разложение для ln(1 + y)
        double y = x - 1;
        double result = 0.0;
        double term = y;
        int n = 1;

        while (Math.abs(term) > EPSILON && n < 100) {
            result += term;
            term *= -y * n / (n + 1);
            n++;
        }
        return result;
    }
}
