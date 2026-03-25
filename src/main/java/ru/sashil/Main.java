package ru.sashil;

import ru.sashil.system.SystemFunction;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Лабораторная работа #2");
        System.out.println("1 - Вычислить значение функции в точке");
        System.out.println("2 - Экспортировать значения в CSV файл");
        System.out.print("Выберите действие: ");

        int choice = scanner.nextInt();

        if (choice == 1) {
            System.out.print("Введите x: ");
            double x = scanner.nextDouble();
            try {
                double result = SystemFunction.calculate(x);
                System.out.printf("f(%.6f) = %.6f%n", x, result);
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        } else if (choice == 2) {
            System.out.print("Введите начальное x: ");
            double start = scanner.nextDouble();
            System.out.print("Введите конечное x: ");
            double end = scanner.nextDouble();
            System.out.print("Введите шаг: ");
            double step = scanner.nextDouble();
            System.out.print("Введите имя файла (например, output.csv): ");
            String filename = scanner.next();

            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                writer.println("x;f(x)");
                for (double x = start; x <= end + step/2; x += step) {
                    try {
                        double result = SystemFunction.calculate(x);
                        writer.printf("%.6f;%.6f%n", x, result);
                    } catch (Exception e) {
                        writer.printf("%.6f;ERROR%n", x);
                    }
                }
                System.out.println("Экспорт завершен. Файл: " + filename);
            } catch (Exception e) {
                System.out.println("Ошибка при записи файла: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
