package ru.sashil.functions.trigonometric;

import ru.sashil.functions.AbstractFunction;

public class CotFunction extends AbstractFunction {

    @Override
    public double calculate(double x) {
        return cot(x);
    }

    public static double cot(double x) {
        double sin = SinFunction.sin(x);
        if (Math.abs(sin) < 1e-12) {
            throw new ArithmeticException("cot(x) не определен при x = π*k");
        }
        return CosFunction.cos(x) / sin;
    }
}
