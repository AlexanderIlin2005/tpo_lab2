package ru.sashil.util;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class ChartGenerator {

    private static final String PLOTS_DIR = System.getProperty("user.dir") + File.separator + "plots" + File.separator;
    private static final int PNG_WIDTH = 900;
    private static final int PNG_HEIGHT = 600;

    public static void displayFunction(String functionName) {
        String csvPath = PLOTS_DIR + functionName + ".csv";
        File file = new File(csvPath);

        if (!file.exists()) {
            System.err.println("Файл не найден: " + csvPath);
            System.err.println("Сначала экспортируйте функции (пункт 3)");
            return;
        }

        boolean needTrim = functionName.equals("Sec") || functionName.equals("Csc") ||
                           functionName.equals("Cot") || functionName.equals("System");

        FunctionGraph graph = new FunctionGraph(
            "График функции " + functionName + "(x)",
            functionName + "(x)",
            csvPath,
            needTrim
        );
        graph.setVisible(true);
    }

    public static void displayAllFunctions() {
        String[] functions = {"Sin", "Cos", "Sec", "Csc", "Cot", "Ln", "Log3", "Log5", "Log10", "System"};

        for (String func : functions) {
            displayFunction(func);
        }
    }

    public static void saveAllAsPNG() throws IOException {
        String[] functions = {"Sin", "Cos", "Sec", "Csc", "Cot", "Ln", "Log3", "Log5", "Log10", "System"};
        String outputDir = PLOTS_DIR + "png" + File.separator;

        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (String func : functions) {
            String csvPath = PLOTS_DIR + func + ".csv";
            String pngPath = outputDir + func + ".png";

            File csvFile = new File(csvPath);
            if (csvFile.exists()) {
                boolean needTrim = func.equals("Sec") || func.equals("Csc") ||
                                   func.equals("Cot") || func.equals("System");

                // Для функций с разрывами используем обрезанный график
                if (needTrim) {
                    // Создаем отдельный график с обрезанными y
                    FunctionGraph.saveAsPNG(csvPath, pngPath, PNG_WIDTH, PNG_HEIGHT);
                } else {
                    FunctionGraph.saveAsPNG(csvPath, pngPath, PNG_WIDTH, PNG_HEIGHT);
                }
                System.out.println("  Сохранён: " + pngPath);
            } else {
                System.out.println("  Файл не найден: " + csvPath);
            }
        }
    }
}
