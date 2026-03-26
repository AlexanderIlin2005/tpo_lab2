package ru.sashil.functions;

/**
 * Общий интерфейс для всех математических функций
 */
public interface MathFunction {
    /**
     * Вычисляет значение функции в точке x
     * @param x аргумент
     * @return значение функции
     * @throws ArithmeticException если функция не определена в точке
     */
    double calculate(double x);

    /**
     * Возвращает имя функции для отображения на графиках
     */
    default String getName() {
        return this.getClass().getSimpleName();
    }
}
