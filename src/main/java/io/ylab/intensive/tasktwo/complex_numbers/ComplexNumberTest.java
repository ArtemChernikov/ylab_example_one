package io.ylab.intensive.tasktwo.complex_numbers;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 12.03.2023
 */
public class ComplexNumberTest {
    public static void main(String[] args) {
        ComplexNumber first = new ComplexNumber(25, 30);
        ComplexNumber second = new ComplexNumber(-25, 40);

        System.out.println(first.sum(second));
        System.out.println(first.subtract(second));
        System.out.println(first.multiply(second));
        System.out.println(first.module());
    }
}
