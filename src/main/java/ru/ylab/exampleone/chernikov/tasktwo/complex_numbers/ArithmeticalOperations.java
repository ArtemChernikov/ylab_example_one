package ru.ylab.exampleone.chernikov.tasktwo.complex_numbers;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 12.03.2023
 */
public class ArithmeticalOperations {
    public static ComplexNumber sum(ComplexNumber first, ComplexNumber second) {
        return new ComplexNumber(first.getRealPart() + second.getRealPart(),
                first.getImaginaryPart() + second.getImaginaryPart());
    }

    public static ComplexNumber subtract(ComplexNumber first, ComplexNumber second) {
        return new ComplexNumber(first.getRealPart() - second.getRealPart(),
                first.getImaginaryPart() - second.getImaginaryPart());
    }

    public static ComplexNumber multiply(ComplexNumber first, ComplexNumber second) {
        return new ComplexNumber(
                first.getRealPart() * second.getRealPart() - first.getImaginaryPart() * second.getImaginaryPart(),
                first.getRealPart() * second.getImaginaryPart() + first.getImaginaryPart() * second.getRealPart());
    }

    public static double module(ComplexNumber number) {
        return Math.sqrt(Math.pow(number.getRealPart(), 2) + Math.pow(number.getImaginaryPart(), 2));
    }
}
