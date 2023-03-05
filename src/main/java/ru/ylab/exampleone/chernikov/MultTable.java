package ru.ylab.exampleone.chernikov;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 05.03.2023
 */
public class MultTable {
    public static void main(String[] args) {
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                System.out.println(i + " x " + j + " = " + i * j);
            }
        }
    }
}
