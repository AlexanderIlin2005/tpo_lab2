package ru.sashil.functions.trigonometric;

public class CotFunction {
    public static double cot(double x) {
        double sin = SinFunction.sin(x);
        if (Math.abs(sin) < 1e-12) {
            throw new ArithmeticException("cot(x) не определен при x = π*k");
        }
        return CosFunction.cos(x) / sin;
    }
}
