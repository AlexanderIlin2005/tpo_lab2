package ru.sashil.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChartGenerator {

    private static final String PLOTS_DIR = System.getProperty("user.dir") + File.separator + "plots" + File.separator;
    private static final int PNG_WIDTH = 900;
    private static final int PNG_HEIGHT = 600;
    private static final int TRIM_MAX_Y = 100;
    private static final List<JFrame> openFrames = new ArrayList<>();

    // Создание датасета из CSV
    private static XYDataset createDataset(String csvFilePath) {
        XYSeries series = new XYSeries("f(x)");

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2) {
                    try {
                        double x = Double.parseDouble(parts[0]);
                        double y = Double.parseDouble(parts[1]);
                        series.add(x, y);
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping malformed line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }

    // Создание графика (общая логика)
    private static JFreeChart createChart(XYDataset dataset, String title, boolean trim) {
        JFreeChart chart = ChartFactory.createXYLineChart(
            title,
            "x",
            "f(x)",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );

        XYPlot plot = chart.getXYPlot();

        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        ValueMarker xMarker = new ValueMarker(0);
        xMarker.setPaint(Color.GRAY);
        xMarker.setStroke(new BasicStroke(1.5f));

        ValueMarker yMarker = new ValueMarker(0);
        yMarker.setPaint(Color.GRAY);
        yMarker.setStroke(new BasicStroke(1.5f));

        plot.addDomainMarker(xMarker);
        plot.addRangeMarker(yMarker);

        plot.getRenderer().setSeriesPaint(0, Color.BLUE);
        plot.getRenderer().setSeriesStroke(0, new BasicStroke(2.0f));

        if (trim) {
            ValueAxis rangeAxis = plot.getRangeAxis();
            rangeAxis.setRange(-TRIM_MAX_Y, TRIM_MAX_Y);
            rangeAxis.setAutoRange(false);
        }

        return chart;
    }

    // Включить GUI режим
    private static void enableGuiMode() {
        System.setProperty("java.awt.headless", "false");
    }

    // Включить headless режим (для сохранения PNG)
    private static void enableHeadlessMode() {
        System.setProperty("java.awt.headless", "true");
    }

    // Отображение графика в окне (GUI режим)
    public static void displayFunction(String functionName) {
        enableGuiMode();

        String csvPath = PLOTS_DIR + functionName + ".csv";
        File file = new File(csvPath);

        if (!file.exists()) {
            System.err.println("Файл не найден: " + csvPath);
            System.err.println("Сначала экспортируйте функции (пункт 3)");
            return;
        }

        boolean needTrim = functionName.equals("Sec") || functionName.equals("Csc") ||
                           functionName.equals("Cot") || functionName.equals("System");

        XYDataset dataset = createDataset(csvPath);
        JFreeChart chart = createChart(dataset, functionName + "(x)", needTrim);

        JFrame frame = new JFrame("График функции " + functionName + "(x)");
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(PNG_WIDTH, PNG_HEIGHT));
        frame.setContentPane(chartPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        openFrames.add(frame);
    }

    // Отображение всех графиков
    public static void displayAllFunctions() {
        String[] functions = {"Sin", "Cos", "Sec", "Csc", "Cot", "Ln", "Log3", "Log5", "Log10", "System"};

        for (String func : functions) {
            displayFunction(func);
        }
    }

    // Закрыть все открытые окна
    public static void closeAllFrames() {
        for (JFrame frame : openFrames) {
            frame.dispose();
        }
        openFrames.clear();
        System.out.println("Все окна графиков закрыты");
    }

    // Сохранение графика в PNG (headless режим - без GUI)
    public static void saveAsPNG(String csvFilePath, String outputPath, int width, int height) throws IOException {
        enableHeadlessMode();

        String functionName = new File(csvFilePath).getName().replace(".csv", "");
        boolean needTrim = functionName.equals("Sec") || functionName.equals("Csc") ||
                           functionName.equals("Cot") || functionName.equals("System");

        XYDataset dataset = createDataset(csvFilePath);
        JFreeChart chart = createChart(dataset, functionName + "(x)", needTrim);

        // Сохраняем в PNG
        ChartUtils.saveChartAsPNG(new File(outputPath), chart, width, height);
    }

    // Сохранение всех графиков
    public static void saveAllAsPNG() throws IOException {
        enableHeadlessMode();

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
                saveAsPNG(csvPath, pngPath, PNG_WIDTH, PNG_HEIGHT);
                System.out.println("  Сохранён: " + pngPath);
            } else {
                System.out.println("  Файл не найден: " + csvPath);
            }
        }
    }
}
