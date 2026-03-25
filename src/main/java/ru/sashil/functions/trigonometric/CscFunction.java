package ru.sashil.functions.trigonometric;

public class CscFunction {
    public static double csc(double x) {
        double sin = SinFunction.sin(x);
        if (Math.abs(sin) < 1e-12) {
            throw new ArithmeticException("csc(x) не определен при x = π*k");
        }
        return 1.0 / sin;
    }
}
