package ru.sashil;

import ru.sashil.functions.MathFunction;
import ru.sashil.functions.trigonometric.*;
import ru.sashil.functions.logarithmic.*;
import ru.sashil.system.SystemFunction;
import ru.sashil.util.CSVWriter;
import ru.sashil.util.ChartGenerator;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final String OUTPUT_DIR = System.getProperty("user.dir") + File.separator + "plots" + File.separator;
    private static final double STEP = 0.05;
    private static final int PRECISION = 12;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n========================================");
            System.out.println("Лабораторная работа #2");
            System.out.println("1 - Вычислить значение функции в точке");
            System.out.println("2 - Экспортировать значения системы функций в CSV");
            System.out.println("3 - Экспортировать все функции в CSV (для построения графиков)");
            System.out.println("4 - Показать график конкретной функции");
            System.out.println("5 - Показать графики всех функций");
            System.out.println("6 - Сохранить все графики в PNG");
            System.out.println("7 - Закрыть все графики");
            System.out.println("0 - Выход");
            System.out.print("Выберите действие: ");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Ошибка: введите число");
                scanner.next();
                continue;
            }

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
                case 4:
                    showSingleGraph(scanner);
                    break;
                case 5:
                    showAllGraphs();
                    break;
                case 6:
                    saveAllGraphsToPNG();
                    break;
                case 7:
                    ChartGenerator.closeAllFrames();
                    break;
                case 0:
                    System.out.println("До свидания!");
                    scanner.close();
                    ChartGenerator.closeAllFrames();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Неверный выбор");
            }
        }
    }

    private static void runSingleCalculation(Scanner scanner) {
        System.out.print("Введите x: ");
        double x = scanner.nextDouble();
        try {
            double result = SystemFunction.calculate(x);
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
            CSVWriter writer = new CSVWriter(x -> SystemFunction.calculate(x), OUTPUT_DIR);
            writer.export(start, end, step, PRECISION);
            System.out.println("Экспорт завершен. Файл: " + writer.getFilePath());
        } catch (Exception e) {
            System.out.println("Ошибка при экспорте: " + e.getMessage());
        }
    }

    private static void exportAllFunctions() {
        System.out.println("Экспорт всех функций...");

        MathFunction[] functions = {
            x -> SinFunction.sin(x),
            x -> CosFunction.cos(x),
            x -> SecFunction.sec(x),
            x -> CscFunction.csc(x),
            x -> CotFunction.cot(x),
            x -> LnFunction.ln(x),
            x -> Log3Function.log3(x),
            x -> Log5Function.log5(x),
            x -> Log10Function.log10(x),
            SystemFunction::calculate
        };

        String[] names = {"Sin", "Cos", "Sec", "Csc", "Cot", "Ln", "Log3", "Log5", "Log10", "System"};

        double start = -10;
        double end = 10;

        for (int i = 0; i < functions.length; i++) {
            try {
                CSVWriter writer = new CSVWriter(functions[i], OUTPUT_DIR);
                writer.export(start, end, STEP, PRECISION);
                System.out.println("  " + names[i] + " -> " + writer.getFilePath());
            } catch (Exception e) {
                System.out.println("  Ошибка при экспорте: " + e.getMessage());
            }
        }

        System.out.println("\n✅ Все файлы сохранены в директории: " + OUTPUT_DIR);
        System.out.println("Для построения графиков используйте пункты 4, 5, 6 или 7");
    }

    private static void showSingleGraph(Scanner scanner) {
        System.out.println("Доступные функции: Sin, Cos, Sec, Csc, Cot, Ln, Log3, Log5, Log10, System");
        System.out.print("Введите название функции: ");
        String funcName = scanner.next();
        ChartGenerator.displayFunction(funcName);
    }

    private static void showAllGraphs() {
        System.out.println("Открытие графиков всех функций...");
        ChartGenerator.displayAllFunctions();
    }

    private static void saveAllGraphsToPNG() {
        System.out.println("Сохранение графиков в PNG...");
        try {
            ChartGenerator.saveAllAsPNG();
            System.out.println("✅ Все графики сохранены в директории: " + OUTPUT_DIR + "png/");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении графиков: " + e.getMessage());
        }
    }
}
