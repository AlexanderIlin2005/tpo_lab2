package ru.sashil.functions.trigonometric;

public class SecFunction {
    public static double sec(double x) {
        double cos = CosFunction.cos(x);
        if (Math.abs(cos) < 1e-12) {
            throw new ArithmeticException("sec(x) не определен при x = π/2 + π*k");
        }
        return 1.0 / cos;
    }
}
