package ru.sashil.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CSVLoader {

    public static Map<Double, Double> loadFunctionValues(String filePath) throws IOException {
        Map<Double, Double> values = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2) {
                    try {
                        double x = Double.parseDouble(parts[0]);
                        double y = Double.parseDouble(parts[1]);
                        values.put(x, y);
                    } catch (NumberFormatException e) {

                    }
                }
            }
        }
        return values;
    }

    public static double interpolate(Map<Double, Double> values, double x) {
        Double exact = values.get(x);
        if (exact != null) {
            return exact;
        }

        Double lower = null;
        Double upper = null;

        for (Double key : values.keySet()) {
            if (key <= x) {
                if (lower == null || key > lower) {
                    lower = key;
                }
            }
            if (key >= x) {
                if (upper == null || key < upper) {
                    upper = key;
                }
            }
        }

        if (lower == null && upper == null) {
            return Double.NaN;
        }
        if (lower == null) {
            return values.get(upper);
        }
        if (upper == null) {
            return values.get(lower);
        }
        if (lower.equals(upper)) {
            return values.get(lower);
        }

        double y1 = values.get(lower);
        double y2 = values.get(upper);
        double t = (x - lower) / (upper - lower);
        return y1 + t * (y2 - y1);
    }
}
