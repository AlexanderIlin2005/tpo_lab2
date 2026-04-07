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

public class FunctionGraph extends JFrame {

    private static final int TRIM_MAX_Y = 100;
    private static final int WIDTH = 900;
    private static final int HEIGHT = 600;

    public FunctionGraph(String title, String chartTitle, String csvFilePath) {
        this(title, chartTitle, csvFilePath, false);
    }

    public FunctionGraph(String title, String chartTitle, String csvFilePath, boolean trim) {
        super(title);
        XYDataset dataset = createDataset(csvFilePath);
        JFreeChart chart = createChart(dataset, chartTitle, trim);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setContentPane(chartPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private XYDataset createDataset(String csvFilePath) {
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

    private JFreeChart createChart(XYDataset dataset, String title, boolean trim) {
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

        // Настройка внешнего вида
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // Линии x=0 и y=0
        ValueMarker xMarker = new ValueMarker(0);
        xMarker.setPaint(Color.GRAY);
        xMarker.setStroke(new BasicStroke(1.5f));

        ValueMarker yMarker = new ValueMarker(0);
        yMarker.setPaint(Color.GRAY);
        yMarker.setStroke(new BasicStroke(1.5f));

        plot.addDomainMarker(xMarker);
        plot.addRangeMarker(yMarker);

        // Цвет линии
        plot.getRenderer().setSeriesPaint(0, Color.BLUE);
        plot.getRenderer().setSeriesStroke(0, new BasicStroke(2.0f));

        if (trim) {
            ValueAxis rangeAxis = plot.getRangeAxis();
            rangeAxis.setRange(-TRIM_MAX_Y, TRIM_MAX_Y);
            rangeAxis.setAutoRange(false);
        }

        return chart;
    }

    public static void saveAsPNG(String csvFilePath, String outputPath, int width, int height) throws IOException {
        XYDataset dataset = new FunctionGraph("", "", csvFilePath).createDataset(csvFilePath);
        JFreeChart chart = ChartFactory.createXYLineChart(
            "График функции",
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

        ChartUtils.saveChartAsPNG(new File(outputPath), chart, width, height);
    }
}
