package io.ylab.intensive.taskthree.file_sort;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 19.03.2023
 */
public class Validator {
    private File file;

    public Validator(File file) {
        this.file = file;
    }

    public boolean isSorted() {
        try (Scanner scanner = new Scanner(new FileInputStream(file))) {
            long prev = Long.MIN_VALUE;
            while (scanner.hasNextLong()) {
                long current = scanner.nextLong();
                if (current < prev) {
                    return false;
                } else {
                    prev = current;
                }
            }
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
