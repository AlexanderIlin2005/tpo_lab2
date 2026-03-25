package ru.sashil.functions.trigonometric;

public class CosFunction {
    public static double cos(double x) {
        return SinFunction.sin(Math.PI / 2 - x);
    }
}
