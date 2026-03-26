package ru.sashil.functions.trigonometric;

import ru.sashil.functions.AbstractFunction;

public class SecFunction extends AbstractFunction {

    @Override
    public double calculate(double x) {
        return sec(x);
    }

    public static double sec(double x) {
        double cos = CosFunction.cos(x);
        if (Math.abs(cos) < 1e-12) {
            throw new ArithmeticException("sec(x) не определен при x = π/2 + π*k");
        }
        return 1.0 / cos;
    }
}
