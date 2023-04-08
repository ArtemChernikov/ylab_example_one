package io.ylab.intensive.tasktwo.complex_numbers;

import java.util.Objects;

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

    /**
     * Метод используется для сложения комплексного числа, на котором вызывается метод
     * и комплексного числа, которое задается в параметрах метода
     *
     * @param number - комплексное число
     * @return - возвращает результат суммирования в виде нового комплексного числа
     */
    public ComplexNumber sum(ComplexNumber number) {
        return new ComplexNumber(this.getRealPart() + number.getRealPart(),
                this.getImaginaryPart() + number.getImaginaryPart());
    }

    /**
     * Метод используется для вычитания из комплексного числа, на котором вызвается метод,
     * комплексного числа, которое задается в параметрах метода
     *
     * @param number - комплексное число
     * @return - возвращает результат вычитания в виде нового комплексного числа
     */
    public ComplexNumber subtract(ComplexNumber number) {
        return new ComplexNumber(this.getRealPart() - number.getRealPart(),
                this.getImaginaryPart() - number.getImaginaryPart());
    }

    /**
     * Метод используется для умножения комплексного числа, на котором вызывается метод
     * с комплексным числом, которое задается в параметрах метода
     *
     * @param number - комплексное число
     * @return - возвращает результат умножения в виде нового комплексного числа
     */
    public ComplexNumber multiply(ComplexNumber number) {
        return new ComplexNumber(
                this.getRealPart() * number.getRealPart() - this.getImaginaryPart() * number.getImaginaryPart(),
                this.getRealPart() * number.getImaginaryPart() + this.getImaginaryPart() * number.getRealPart());
    }

    /**
     * Метод используется для вычисления модуля комплексного числа
     *
     * @return - возвращает модуль комплексного числа
     */
    public double module() {
        return Math.sqrt(Math.pow(this.getRealPart(), 2) + Math.pow(this.getImaginaryPart(), 2));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexNumber that = (ComplexNumber) o;
        return Double.compare(that.realPart, realPart) == 0 && Double.compare(that.imaginaryPart, imaginaryPart) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(realPart, imaginaryPart);
    }
}
