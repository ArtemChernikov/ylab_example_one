package ru.ylab.exampleone.chernikov.taskone;

import java.util.Scanner;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 03.03.2023
 */
public class Stars {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            String template = scanner.next();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    System.out.print(template);
                }
                System.out.println();
            }
        }
    }
}
