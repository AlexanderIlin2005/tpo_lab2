package ru.sashil.system.parts;

import ru.sashil.functions.trigonometric.*;

public class TrigonometricPart {

    public static double calculate(double x) {
        // Формула: (((((sec(x)^2) * cot(x))^3) + cot(x)) - ((sin(x) / (csc(x)^2))^2))
        double sec = SecFunction.sec(x);
        double cot = CotFunction.cot(x);
        double sin = SinFunction.sin(x);
        double csc = CscFunction.csc(x);

        double secSq = sec * sec;
        double secSqMulCot = secSq * cot;
        double secSqMulCotCubed = Math.pow(secSqMulCot, 3);
        double firstPart = secSqMulCotCubed + cot;

        double cscSq = csc * csc;
        double sinDivCscSq = sin / cscSq;
        double secondPart = sinDivCscSq * sinDivCscSq;

        return firstPart - secondPart;
    }
}
