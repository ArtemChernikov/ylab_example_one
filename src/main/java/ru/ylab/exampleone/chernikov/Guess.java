package ru.ylab.exampleone.chernikov;

import java.util.Random;
import java.util.Scanner;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 05.03.2023
 */
public class Guess {
    public static void main(String[] args) {
        int number = new Random().nextInt(99) + 1; // здесь загадывается число от 1 до 99
        int maxAttempts = 10; // здесь задается количество попыток
        System.out.println("Я загадал число. У тебя " + maxAttempts + " попыток угадать.");
        try (Scanner scanner = new Scanner(System.in)) {
            int inputNumber = 0;
            for (int i = maxAttempts - 1; i >= 0; i--) {
                inputNumber = scanner.nextInt();
                if (inputNumber == number) {
                    System.out.println("Ты угадал с " + (maxAttempts - i) + " попытки!");
                    break;
                } else if (inputNumber > number) {
                    System.out.println("Мое число меньше! Осталось " + i + " попыток.");
                } else {
                    System.out.println("Мое число больше! Осталось " + i + " попыток.");
                }
            }
            if (inputNumber != number) {
                System.out.println("Ты не угадал.");
            }
        }
    }
}
