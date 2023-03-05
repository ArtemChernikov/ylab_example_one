package ru.ylab.exampleone.chernikov;

import java.util.Scanner;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 03.03.2023
 */
public class Pell {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            if (n == 0) {
                System.out.println(0);
            } else {
                long[] numbers = new long[n + 1];
                numbers[0] = 0;
                numbers[1] = 1;
                for (int i = 2; i < numbers.length; i++) {
                    long a = numbers[i - 2];
                    long b = numbers[i - 1];
                    numbers[i] = b * 2 + a;
                }
                System.out.println(numbers[n]);
            }
        }
    }
}
