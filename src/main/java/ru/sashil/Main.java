package ru.sashil;

import ru.sashil.functions.MathFunction;
import ru.sashil.functions.trigonometric.*;
import ru.sashil.functions.logarithmic.*;
import ru.sashil.system.SystemFunction;
import ru.sashil.util.CSVWriter;

import java.io.File;
import java.util.Scanner;

public class Main {

    private static final String OUTPUT_DIR = System.getProperty("user.dir") + File.separator + "plots" + File.separator;
    private static final double STEP = 0.05;
    private static final int PRECISION = 12;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("1 - Вычислить значение функции в точке");
        System.out.println("2 - Экспортировать значения системы функций в CSV");
        System.out.println("3 - Экспортировать все функции в CSV");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                runSingleCalculation(scanner);
                break;
            case 2:
                exportSystemFunction(scanner);
                break;
            case 3:
                exportAllFunctions();
                break;
            default:
                System.out.println("Неверный выбор");
        }

        scanner.close();
    }

    private static void runSingleCalculation(Scanner scanner) {
        System.out.print("Введите x: ");
        double x = scanner.nextDouble();
        try {
            SystemFunction system = new SystemFunction();
            double result = system.calculate(x);
            System.out.printf("f(%.6f) = %.6f%n", x, result);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void exportSystemFunction(Scanner scanner) {
        System.out.print("Введите начальное x: ");
        double start = scanner.nextDouble();
        System.out.print("Введите конечное x: ");
        double end = scanner.nextDouble();
        System.out.print("Введите шаг: ");
        double step = scanner.nextDouble();

        try {
            SystemFunction system = new SystemFunction();
            CSVWriter writer = new CSVWriter(system, OUTPUT_DIR);
            writer.export(start, end, step, PRECISION);
            System.out.println("Экспорт завершен. Файл: " + writer.getFilePath());
        } catch (Exception e) {
            System.out.println("Ошибка при экспорте: " + e.getMessage());
        }
    }

    private static void exportAllFunctions() {

        MathFunction[] functions = {
            new SinFunction(),
            new CosFunction(),
            new SecFunction(),
            new CscFunction(),
            new CotFunction(),
            new LnFunction(),
            new Log3Function(),
            new Log5Function(),
            new Log10Function(),
            new SystemFunction()
        };

        double start = -10;
        double end = 10;

        for (MathFunction func : functions) {
            try {
                CSVWriter writer = new CSVWriter(func, OUTPUT_DIR);
                writer.export(start, end, STEP, PRECISION);
                System.out.println("  " + func.getName() + " -> " + writer.getFilePath());
            } catch (Exception e) {
                System.out.println("  " + func.getName() + " -> ошибка: " + e.getMessage());
            }
        }

        System.out.println("\nВсе файлы сохранены в директории: " + OUTPUT_DIR);
    }
}
