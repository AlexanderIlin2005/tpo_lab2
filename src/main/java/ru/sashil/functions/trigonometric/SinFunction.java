package ru.sashil.functions.trigonometric;

public class SinFunction {
    private static final double EPSILON = 1e-12;

    public static double sin(double x) {
        x = normalizeAngle(x);

        double result = 0.0;
        double term = x;
        int n = 1;

        while (Math.abs(term) > EPSILON) {
            result += term;
            term *= -x * x / ((2 * n) * (2 * n + 1));
            n++;
        }
        return result;
    }

    private static double normalizeAngle(double x) {
        double pi = Math.PI;
        x = x % (2 * pi);
        if (x > pi) x -= 2 * pi;
        if (x < -pi) x += 2 * pi;
        return x;
    }
}
