package ru.ylab.exampleone.chernikov.tasktwo.complex_numbers;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 12.03.2023
 */
public class ArithmeticalOperationsTest {
    public static void main(String[] args) {
        ComplexNumber first = new ComplexNumber(25, 30);
        ComplexNumber second = new ComplexNumber(-25, 40);

        System.out.println(ArithmeticalOperations.sum(first, second));
        System.out.println(ArithmeticalOperations.subtract(first, second));
        System.out.println(ArithmeticalOperations.multiply(first, second));
        System.out.println(ArithmeticalOperations.module(first));
    }
}
