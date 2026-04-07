package ru.sashil.system;

import ru.sashil.system.parts.TrigonometricPart;
import ru.sashil.system.parts.LogarithmicPart;

public class SystemFunction {

    public static double calculate(double x) {
        if (x < 0) {
            return TrigonometricPart.calculate(x);
        } else if (x > 0) {
            if (Math.abs(x - 1.0) < 1e-12) {
                throw new ArithmeticException("Функция не определена в точке x = 1 (деление на ноль)");
            }
            return LogarithmicPart.calculate(x);
        } else {
            throw new ArithmeticException("Функция не определена в точке x = 0");
        }
    }
}
