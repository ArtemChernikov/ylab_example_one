package ru.ylab.exampleone.chernikov.tasktwo.sequences;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 11.03.2023
 */
public class SequencesGeneratorImpl implements SequencesGenerator {
    @Override
    public void a(int n) {
        long number = 0;
        for (int i = 0; i < n; i++) {
            number += 2;
            System.out.println(number);
        }
    }

    @Override
    public void b(int n) {
        long number = 1;
        for (int i = 0; i < n; i++) {
            System.out.println(number);
            number += 2;
        }
    }

    @Override
    public void c(int n) {
        for (int i = 1; i <= n; i++) {
            System.out.println(Math.round(Math.pow(i, 2)));
        }
    }

    @Override
    public void d(int n) {
        for (int i = 1; i <= n; i++) {
            System.out.println(Math.round(Math.pow(i, 3)));
        }
    }

    @Override
    public void e(int n) {
        int number = -1;
        for (int i = 0; i < n; i++) {
            number /= -1;
            System.out.println(number);
        }
    }

    @Override
    public void f(int n) {
        for (int i = 1; i <= n; i++) {
            int number = i;
            if (number % 2 == 0) {
                number /= -1;
            }
            System.out.println(number);
        }
    }

    @Override
    public void g(int n) {
        for (int i = 1; i <= n; i++) {
            long number = Math.round(Math.pow(i, 2));
            if (number % 2 == 0) {
                number /= -1;
            }
            System.out.println(number);
        }
    }

    @Override
    public void h(int n) {
        int number = 1;
        for (int i = 1; i <= n; i++) {
            if (i % 2 != 0) {
                System.out.println(number++);
            } else {
                System.out.println(0);
            }
        }
    }

    @Override
    public void i(int n) {
        long number = 1;
        for (int i = 1; i <= n; i++) {
            number *= i;
            System.out.println(number);
        }
    }

    @Override
    public void j(int n) {
        long number = 1;
        long tempNumber = 0;
        for (int i = 0; i < n; i++) {
            System.out.println(number);
            long temp = number;
            number += tempNumber;
            tempNumber = temp;
        }
    }
}
