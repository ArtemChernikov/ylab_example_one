package io.ylab.intensive.taskthree.file_sort;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 19.03.2023
 */
public class Generator {
    public File generate(String name, int count) throws IOException {
        Random random = new Random();
        File file = new File(name);
        try (PrintWriter pw = new PrintWriter(file)) {
            for (int i = 0; i < count; i++) {
                pw.println(random.nextLong());
            }
            pw.flush();
        }
        return file;
    }
}
