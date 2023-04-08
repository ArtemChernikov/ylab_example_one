package io.ylab.intensive.taskthree.file_sort;

import java.io.File;
import java.io.IOException;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 19.03.2023
 */
public class Test {
    public static void main(String[] args) throws IOException {
        /* Количество эементов для сортировки */
        int countData = 375_000_000;
        File dataFile = new Generator().generate("data.txt", countData);
        System.out.println(new Validator(dataFile).isSorted());
        File sortedFile = new Sorter(countData / 10).sortFile(dataFile);
        System.out.println(new Validator(sortedFile).isSorted());
    }
}
