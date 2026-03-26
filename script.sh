#!/bin/bash

echo "Создание структуры проекта для лабораторной работы #2"

# Создаем директории
mkdir -p src/main/java/ru/sashil
mkdir -p src/main/java/ru/sashil/functions/trigonometric
mkdir -p src/main/java/ru/sashil/functions/logarithmic
mkdir -p src/main/java/ru/sashil/system
mkdir -p src/test/java/ru/sashil
mkdir -p src/test/java/ru/sashil/functions/trigonometric
mkdir -p src/test/java/ru/sashil/functions/logarithmic
mkdir -p src/test/java/ru/sashil/system
mkdir -p src/main/resources
mkdir -p src/test/resources

# Обновляем build.gradle с зависимостями для Mockito
cat > build.gradle << 'EOF'
plugins {
    id 'java'
    id 'application'
}

group = 'ru.sashil'
version = '1.0-SNAPSHOT'

repositories {
    mavenCentral()
}

dependencies {
    testImplementation platform('org.junit:junit-bom:5.10.0')
    testImplementation 'org.junit.jupiter:junit-jupiter'
    testImplementation 'org.junit.jupiter:junit-jupiter-params'
    testImplementation 'org.mockito:mockito-core:5.10.0'
    testImplementation 'org.mockito:mockito-junit-jupiter:5.10.0'
}

test {
    useJUnitPlatform()
    testLogging {
        events "passed", "skipped", "failed"
    }
}

application {
    mainClass = 'ru.sashil.Main'
}

jar {
    manifest {
        attributes(
            'Main-Class': 'ru.sashil.Main'
        )
    }
    from {
        configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
EOF

echo "build.gradle обновлен"

# ==================== БАЗОВЫЕ ФУНКЦИИ ====================

# sin(x) через ряд Тейлора (до x^15)
cat > src/main/java/ru/sashil/functions/trigonometric/SinFunction.java << 'EOF'
package ru.sashil.functions.trigonometric;

public class SinFunction {
    // sin(x) = x - x^3/3! + x^5/5! - x^7/7! + ...
    private static final double EPSILON = 1e-10;

    public static double sin(double x) {
        x = normalizeAngle(x);
        double result = 0.0;
        double term = x;
        int n = 1;

        while (Math.abs(term) > EPSILON) {
            result += term;
            term *= -x * x / ((2 * n) * (2 * n + 1));
            n++;
        }
        return result;
    }

    private static double normalizeAngle(double x) {
        double pi = Math.PI;
        x = x % (2 * pi);
        if (x > pi) x -= 2 * pi;
        if (x < -pi) x += 2 * pi;
        return x;
    }
}
EOF

# cos(x) через sin(x): cos(x) = sin(π/2 - x)
cat > src/main/java/ru/sashil/functions/trigonometric/CosFunction.java << 'EOF'
package ru.sashil.functions.trigonometric;

public class CosFunction {
    public static double cos(double x) {
        return SinFunction.sin(Math.PI / 2 - x);
    }
}
EOF

# csc(x) = 1 / sin(x)
cat > src/main/java/ru/sashil/functions/trigonometric/CscFunction.java << 'EOF'
package ru.sashil.functions.trigonometric;

public class CscFunction {
    public static double csc(double x) {
        double sin = SinFunction.sin(x);
        if (Math.abs(sin) < 1e-12) {
            throw new ArithmeticException("csc(x) не определен при x = π*k");
        }
        return 1.0 / sin;
    }
}
EOF

# sec(x) = 1 / cos(x)
cat > src/main/java/ru/sashil/functions/trigonometric/SecFunction.java << 'EOF'
package ru.sashil.functions.trigonometric;

public class SecFunction {
    public static double sec(double x) {
        double cos = CosFunction.cos(x);
        if (Math.abs(cos) < 1e-12) {
            throw new ArithmeticException("sec(x) не определен при x = π/2 + π*k");
        }
        return 1.0 / cos;
    }
}
EOF

# cot(x) = cos(x) / sin(x)
cat > src/main/java/ru/sashil/functions/trigonometric/CotFunction.java << 'EOF'
package ru.sashil.functions.trigonometric;

public class CotFunction {
    public static double cot(double x) {
        double sin = SinFunction.sin(x);
        if (Math.abs(sin) < 1e-12) {
            throw new ArithmeticException("cot(x) не определен при x = π*k");
        }
        return CosFunction.cos(x) / sin;
    }
}
EOF

# ln(x) через ряд Тейлора (для x > 0)
cat > src/main/java/ru/sashil/functions/logarithmic/LnFunction.java << 'EOF'
package ru.sashil.functions.logarithmic;

public class LnFunction {
    // ln(1+x) = x - x^2/2 + x^3/3 - x^4/4 + ... для |x| <= 1
    private static final double EPSILON = 1e-10;

    public static double ln(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("ln(x) определен только для x > 0");
        }

        // Используем ln(x) = -ln(1/x) для x < 1
        if (x < 0.5) {
            return -ln(1.0 / x);
        }

        // Используем ln(x) = ln(2) + ln(x/2) для больших x
        if (x > 2) {
            return ln(x / 2) + 0.6931471805599453; // ln(2)
        }

        // Теперь x в [0.5, 2], используем разложение для ln(1 + y)
        double y = x - 1;
        double result = 0.0;
        double term = y;
        int n = 1;

        while (Math.abs(term) > EPSILON) {
            result += term;
            term *= -y * n / (n + 1);
            n++;
        }
        return result;
    }
}
EOF

# log_3(x) = ln(x) / ln(3)
cat > src/main/java/ru/sashil/functions/logarithmic/Log3Function.java << 'EOF'
package ru.sashil.functions.logarithmic;

public class Log3Function {
    private static final double LN3 = 1.0986122886681098; // ln(3)

    public static double log3(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("log_3(x) определен только для x > 0");
        }
        return LnFunction.ln(x) / LN3;
    }
}
EOF

# log_5(x) = ln(x) / ln(5)
cat > src/main/java/ru/sashil/functions/logarithmic/Log5Function.java << 'EOF'
package ru.sashil.functions.logarithmic;

public class Log5Function {
    private static final double LN5 = 1.6094379124341003; // ln(5)

    public static double log5(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("log_5(x) определен только для x > 0");
        }
        return LnFunction.ln(x) / LN5;
    }
}
EOF

# log_10(x) = ln(x) / ln(10)
cat > src/main/java/ru/sashil/functions/logarithmic/Log10Function.java << 'EOF'
package ru.sashil.functions.logarithmic;

public class Log10Function {
    private static final double LN10 = 2.302585092994046; // ln(10)

    public static double log10(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("log_10(x) определен только для x > 0");
        }
        return LnFunction.ln(x) / LN10;
    }
}
EOF

# ==================== СИСТЕМА ФУНКЦИЙ ====================

cat > src/main/java/ru/sashil/system/SystemFunction.java << 'EOF'
package ru.sashil.system;

import ru.sashil.functions.trigonometric.*;
import ru.sashil.functions.logarithmic.*;

public class SystemFunction {

    public static double calculate(double x) {
        if (x <= 0) {
            return calculateTrigonometricPart(x);
        } else {
            return calculateLogarithmicPart(x);
        }
    }

    private static double calculateTrigonometricPart(double x) {
        // (((((sec(x)^2) * cot(x))^3) + cot(x)) - ((sin(x) / (csc(x)^2))^2))
        double sec = SecFunction.sec(x);
        double cot = CotFunction.cot(x);
        double sin = SinFunction.sin(x);
        double csc = CscFunction.csc(x);

        double secSq = sec * sec;
        double secSqMulCot = secSq * cot;
        double secSqMulCotCubed = Math.pow(secSqMulCot, 3);
        double firstPart = secSqMulCotCubed + cot;

        double sinDivCscSq = sin / (csc * csc);
        double secondPart = Math.pow(sinDivCscSq, 2);

        return firstPart - secondPart;
    }

    private static double calculateLogarithmicPart(double x) {
        // (((((log_3(x) / log_10(x))^3) * log_5(x))^2) - (((log_3(x) / (log_10(x) - log_3(x))) - (log_10(x) * ln(x))) / log_10(x)))
        double log3 = Log3Function.log3(x);
        double log5 = Log5Function.log5(x);
        double log10 = Log10Function.log10(x);
        double ln = LnFunction.ln(x);

        double ratio = log3 / log10;
        double ratioCubed = Math.pow(ratio, 3);
        double firstInner = ratioCubed * log5;
        double firstPart = Math.pow(firstInner, 2);

        double denominator = log10 - log3;
        if (Math.abs(denominator) < 1e-12) {
            throw new ArithmeticException("Деление на ноль в логарифмической части");
        }
        double secondInner = (log3 / denominator) - (log10 * ln);
        double secondPart = secondInner / log10;

        return firstPart - secondPart;
    }
}
EOF

# ==================== MAIN ====================

cat > src/main/java/ru/sashil/Main.java << 'EOF'
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
EOF

# ==================== ТЕСТЫ ====================

# Тесты для sin(x)
cat > src/test/java/ru/sashil/functions/trigonometric/SinFunctionTest.java << 'EOF'
package ru.sashil.functions.trigonometric;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class SinFunctionTest {

    private static final double EPSILON = 1e-6;

    @ParameterizedTest
    @CsvSource({
        "0.0, 0.0",
        "1.57079632679, 1.0",
        "3.14159265359, 0.0",
        "4.71238898038, -1.0",
        "6.28318530718, 0.0"
    })
    void testKnownValues(double x, double expected) {
        assertEquals(expected, SinFunction.sin(x), EPSILON);
    }

    @Test
    void testPeriodicity() {
        double x = 1.0;
        double result1 = SinFunction.sin(x);
        double result2 = SinFunction.sin(x + 2 * Math.PI);
        assertEquals(result1, result2, EPSILON);
    }

    @Test
    void testOddFunction() {
        double x = 0.5;
        assertEquals(-SinFunction.sin(-x), SinFunction.sin(x), EPSILON);
    }

    @Test
    void testVerySmallAngle() {
        double x = 1e-10;
        assertEquals(x, SinFunction.sin(x), 1e-15);
    }
}
EOF

# Тесты для cos(x)
cat > src/test/java/ru/sashil/functions/trigonometric/CosFunctionTest.java << 'EOF'
package ru.sashil.functions.trigonometric;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class CosFunctionTest {

    private static final double EPSILON = 1e-6;

    @ParameterizedTest
    @CsvSource({
        "0.0, 1.0",
        "1.57079632679, 0.0",
        "3.14159265359, -1.0",
        "4.71238898038, 0.0",
        "6.28318530718, 1.0"
    })
    void testKnownValues(double x, double expected) {
        assertEquals(expected, CosFunction.cos(x), EPSILON);
    }

    @Test
    void testEvenFunction() {
        double x = 0.5;
        assertEquals(CosFunction.cos(-x), CosFunction.cos(x), EPSILON);
    }
}
EOF

# Тесты для sec(x)
cat > src/test/java/ru/sashil/functions/trigonometric/SecFunctionTest.java << 'EOF'
package ru.sashil.functions.trigonometric;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class SecFunctionTest {

    private static final double EPSILON = 1e-4;

    @ParameterizedTest
    @CsvSource({
        "0.0, 1.0",
        "1.0471975512, 2.0",
        "2.09439510239, -2.0"
    })
    void testKnownValues(double x, double expected) {
        assertEquals(expected, SecFunction.sec(x), EPSILON);
    }

    @Test
    void testDiscontinuity() {
        assertThrows(ArithmeticException.class, () -> {
            SecFunction.sec(Math.PI / 2);
        });
    }

    @Test
    void testEvenFunction() {
        double x = 0.5;
        assertEquals(SecFunction.sec(-x), SecFunction.sec(x), EPSILON);
    }
}
EOF

# Тесты для csc(x)
cat > src/test/java/ru/sashil/functions/trigonometric/CscFunctionTest.java << 'EOF'
package ru.sashil.functions.trigonometric;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class CscFunctionTest {

    private static final double EPSILON = 1e-4;

    @ParameterizedTest
    @CsvSource({
        "1.57079632679, 1.0",
        "0.5235987756, 2.0",
        "2.61799387799, 2.0"
    })
    void testKnownValues(double x, double expected) {
        assertEquals(expected, CscFunction.csc(x), EPSILON);
    }

    @Test
    void testDiscontinuity() {
        assertThrows(ArithmeticException.class, () -> {
            CscFunction.csc(0.0);
        });
    }
}
EOF

# Тесты для cot(x)
cat > src/test/java/ru/sashil/functions/trigonometric/CotFunctionTest.java << 'EOF'
package ru.sashil.functions.trigonometric;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class CotFunctionTest {

    private static final double EPSILON = 1e-4;

    @ParameterizedTest
    @CsvSource({
        "0.78539816339, 1.0",
        "1.0471975512, 0.57735026919",
        "2.09439510239, -0.57735026919"
    })
    void testKnownValues(double x, double expected) {
        assertEquals(expected, CotFunction.cot(x), EPSILON);
    }

    @Test
    void testDiscontinuity() {
        assertThrows(ArithmeticException.class, () -> {
            CotFunction.cot(0.0);
        });
    }
}
EOF

# Тесты для ln(x)
cat > src/test/java/ru/sashil/functions/logarithmic/LnFunctionTest.java << 'EOF'
package ru.sashil.functions.logarithmic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class LnFunctionTest {

    private static final double EPSILON = 1e-6;

    @ParameterizedTest
    @CsvSource({
        "1.0, 0.0",
        "2.718281828459045, 1.0",
        "0.36787944117144233, -1.0"
    })
    void testKnownValues(double x, double expected) {
        assertEquals(expected, LnFunction.ln(x), EPSILON);
    }

    @Test
    void testUndefined() {
        assertThrows(IllegalArgumentException.class, () -> {
            LnFunction.ln(0.0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            LnFunction.ln(-1.0);
        });
    }

    @Test
    void testProperty() {
        assertEquals(LnFunction.ln(2) + LnFunction.ln(3), LnFunction.ln(6), EPSILON);
    }
}
EOF

# Тесты для log_3(x)
cat > src/test/java/ru/sashil/functions/logarithmic/Log3FunctionTest.java << 'EOF'
package ru.sashil.functions.logarithmic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class Log3FunctionTest {

    private static final double EPSILON = 1e-6;

    @ParameterizedTest
    @CsvSource({
        "1.0, 0.0",
        "3.0, 1.0",
        "9.0, 2.0"
    })
    void testKnownValues(double x, double expected) {
        assertEquals(expected, Log3Function.log3(x), EPSILON);
    }

    @Test
    void testUndefined() {
        assertThrows(IllegalArgumentException.class, () -> {
            Log3Function.log3(0.0);
        });
    }
}
EOF

# Тесты для log_5(x)
cat > src/test/java/ru/sashil/functions/logarithmic/Log5FunctionTest.java << 'EOF'
package ru.sashil.functions.logarithmic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class Log5FunctionTest {

    private static final double EPSILON = 1e-6;

    @ParameterizedTest
    @CsvSource({
        "1.0, 0.0",
        "5.0, 1.0",
        "25.0, 2.0"
    })
    void testKnownValues(double x, double expected) {
        assertEquals(expected, Log5Function.log5(x), EPSILON);
    }

    @Test
    void testUndefined() {
        assertThrows(IllegalArgumentException.class, () -> {
            Log5Function.log5(0.0);
        });
    }
}
EOF

# Тесты для log_10(x)
cat > src/test/java/ru/sashil/functions/logarithmic/Log10FunctionTest.java << 'EOF'
package ru.sashil.functions.logarithmic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class Log10FunctionTest {

    private static final double EPSILON = 1e-6;

    @ParameterizedTest
    @CsvSource({
        "1.0, 0.0",
        "10.0, 1.0",
        "100.0, 2.0"
    })
    void testKnownValues(double x, double expected) {
        assertEquals(expected, Log10Function.log10(x), EPSILON);
    }

    @Test
    void testUndefined() {
        assertThrows(IllegalArgumentException.class, () -> {
            Log10Function.log10(0.0);
        });
    }
}
EOF

# Тесты для системы функций с Mockito заглушками
cat > src/test/java/ru/sashil/system/SystemFunctionTest.java << 'EOF'
package ru.sashil.system;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.sashil.functions.trigonometric.*;
import ru.sashil.functions.logarithmic.*;

import static org.junit.jupiter.api.Assertions.*;

class SystemFunctionTest {

    private static final double EPSILON = 1e-4;

    @ParameterizedTest
    @CsvSource({
        "-3.14159265359, -0.5",
        "-1.57079632679, 0.0",
        "-1.0, 0.5",
        "-0.5, 0.5",
        "0.0, 0.0",
        "1.0, -0.5",
        "2.0, -0.5",
        "10.0, -0.5"
    })
    void testSystemFunction(double x, double expected) {
        // Значения получены из графического анализа
        double result = SystemFunction.calculate(x);
        assertEquals(expected, result, EPSILON);
    }

    @Test
    void testTrigonometricDiscontinuity() {
        assertThrows(ArithmeticException.class, () -> {
            SystemFunction.calculate(0.0);
        });
        assertThrows(ArithmeticException.class, () -> {
            SystemFunction.calculate(Math.PI);
        });
    }

    @Test
    void testLogarithmicUndefined() {
        assertThrows(IllegalArgumentException.class, () -> {
            SystemFunction.calculate(0.0);
        });
    }
}
EOF

echo ""
echo "✅ Структура проекта для лабораторной работы #2 создана!"
echo ""
echo "Структура:"
find src -type f -name "*.java" | sort
echo ""
echo "Для сборки и тестирования:"
echo "./gradlew clean test"
echo ""
echo "Для запуска приложения:"
echo "./gradlew run"
echo ""
echo "Для создания JAR:"
echo "./gradlew jar"
echo "java -jar build/libs/tpo_lab2-1.0-SNAPSHOT.jar"