package ru.sashil.functions.trigonometric;

import ru.sashil.functions.AbstractFunction;

public class CosFunction extends AbstractFunction {

    @Override
    public double calculate(double x) {
        return cos(x);
    }

    public static double cos(double x) {
        return SinFunction.sin(Math.PI / 2 - x);
    }
}
