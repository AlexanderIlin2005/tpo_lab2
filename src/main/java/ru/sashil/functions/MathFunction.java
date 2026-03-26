package ru.sashil.functions;


public interface MathFunction {

    double calculate(double x);


    default String getName() {
        return this.getClass().getSimpleName();
    }
}
