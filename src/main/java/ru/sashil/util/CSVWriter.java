package ru.sashil.util;

import ru.sashil.functions.MathFunction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Универсальный класс для экспорта значений функции в CSV файл
 */
public class CSVWriter {
    private final MathFunction function;
    private final String outputDir;
    private final String separator;

    /**
     * @param function функция для экспорта
     * @param outputDir директория для сохранения файлов
     */
    public CSVWriter(MathFunction function, String outputDir) {
        this(function, outputDir, ";");
    }

    /**
     * @param function функция для экспорта
     * @param outputDir директория для сохранения файлов
     * @param separator разделитель полей (например, ";" или ",")
     */
    public CSVWriter(MathFunction function, String outputDir, String separator) {
        this.function = function;
        this.outputDir = outputDir.endsWith(File.separator) ? outputDir : outputDir + File.separator;
        this.separator = separator;
    }

    /**
     * Экспортирует значения функции в CSV файл
     * @param start начальное значение x
     * @param end конечное значение x
     * @param step шаг
     * @throws IOException при ошибках записи
     */
    public void export(double start, double end, double step) throws IOException {
        export(start, end, step, null);
    }

    /**
     * Экспортирует значения функции в CSV файл с заданной точностью
     * @param start начальное значение x
     * @param end конечное значение x
     * @param step шаг
     * @param precision точность (если null, используется double)
     * @throws IOException при ошибках записи
     */
    public void export(double start, double end, double step, BigDecimal precision) throws IOException {
        String filename = outputDir + function.getName() + ".csv";
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("x" + separator + function.getName() + "(x)");

            for (double x = start; x <= end + step / 2; x += step) {
                try {
                    double value = function.calculate(x);
                    if (precision != null) {
                        BigDecimal bd = BigDecimal.valueOf(value);
                        bd = bd.setScale(precision.scale(), RoundingMode.HALF_UP);
                        writer.println(formatDouble(x) + separator + bd.toPlainString());
                    } else {
                        writer.println(formatDouble(x) + separator + formatDouble(value));
                    }
                } catch (Exception e) {
                    writer.println(formatDouble(x) + separator + "NaN");
                }
            }
        }
    }

    private String formatDouble(double d) {
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            return "NaN";
        }
        // Округляем до 6 знаков для читаемости
        return String.format("%.6f", d).replace(",", ".");
    }

    /**
     * Возвращает полный путь к созданному файлу
     */
    public String getFilePath() {
        return outputDir + function.getName() + ".csv";
    }
}
