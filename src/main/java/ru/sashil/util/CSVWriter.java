package ru.sashil.util;

import ru.sashil.functions.MathFunction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CSVWriter {
    private final MathFunction function;
    private final String outputDir;
    private final String separator;
    private static final int DEFAULT_PRECISION = 12;

    public CSVWriter(MathFunction function, String outputDir) {
        this(function, outputDir, ";");
    }

    public CSVWriter(MathFunction function, String outputDir, String separator) {
        this.function = function;
        this.outputDir = outputDir.endsWith(File.separator) ? outputDir : outputDir + File.separator;
        this.separator = separator;
    }

    public void export(double start, double end, double step) throws IOException {
        export(start, end, step, DEFAULT_PRECISION);
    }

    public void export(double start, double end, double step, int precision) throws IOException {
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
                    writer.println(formatDouble(x, precision) + separator + formatDouble(value, precision));
                } catch (Exception e) {
                    writer.println(formatDouble(x, precision) + separator + "NaN");
                }
            }
        }
    }

    private String formatDouble(double d, int precision) {
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            return "NaN";
        }
        BigDecimal bd = BigDecimal.valueOf(d);
        bd = bd.setScale(precision, RoundingMode.HALF_UP);
        return bd.toPlainString();
    }

    public String getFilePath() {
        return outputDir + function.getName() + ".csv";
    }
}
