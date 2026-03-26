package ru.sashil.functions;

/**
 * Абстрактный базовый класс для всех функций
 */
public abstract class AbstractFunction implements MathFunction {

    @Override
    public abstract double calculate(double x);

    @Override
    public String getName() {
        return this.getClass().getSimpleName().replace("Function", "");
    }
}
