package ru.ylab.exampleone.chernikov.tasktwo.complex_numbers;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 12.03.2023
 */
public class ComplexNumber {
    /**
     * Поле действительная часть
     */
    private double realPart;
    /**
     * Поле мнимая часть
     */
    private double imaginaryPart;

    public ComplexNumber(double realPart) {
        this.realPart = realPart;
    }

    public ComplexNumber(double realPart, double imaginaryPart) {
        this.realPart = realPart;
        this.imaginaryPart = imaginaryPart;
    }

    public double getRealPart() {
        return realPart;
    }

    public void setRealPart(double realPart) {
        this.realPart = realPart;
    }

    public double getImaginaryPart() {
        return imaginaryPart;
    }

    public void setImaginaryPart(double imaginaryPart) {
        this.imaginaryPart = imaginaryPart;
    }

    @Override
    public String toString() {
        if (realPart == 0 && imaginaryPart == 0) {
            return "z = 0";
        }
        String sign = imaginaryPart < 0 || imaginaryPart == 0 || realPart == 0 ? "" : "+";
        String real = realPart == 0 ? "" : realPart + "";
        String imaginary = imaginaryPart == 0 ? "" : imaginaryPart + "i";
        return "z = " + real + sign + imaginary;
    }
}
