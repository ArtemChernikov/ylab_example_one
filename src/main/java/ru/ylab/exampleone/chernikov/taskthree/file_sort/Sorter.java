package ru.ylab.exampleone.chernikov.taskthree.file_sort;

import java.io.*;
import java.util.*;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 19.03.2023
 */
public class Sorter {
    /**
     * Поле размер блока при делении файла на части
     */
    private final int blockSize;

    public Sorter(int blockSize) {
        this.blockSize = blockSize;
    }

    public File sortFile(File dataFile) throws IOException {
        File sortedFile = new File("output.txt");
        List<File> sortedBlocks = readBlocks(dataFile);
        mergeSortedFiles(sortedBlocks, sortedFile);
        return sortedFile;
    }

    /**
     * Метод используется для чтения и сортировки данных в файле
     * и дальнейшего их распределения по новым временным файлам (блокам)
     *
     * @param inputFile - файл для деления и сортировки
     * @return - возвращает список файлов (блоков)
     * @throws IOException - может выбросить {@link IOException}
     */
    private List<File> readBlocks(File inputFile) throws IOException {
        List<File> sortedBlocks = new ArrayList<>();
        List<Long> block = new ArrayList<>(blockSize);
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty() && !line.equals("\n")) {
                block.add(Long.parseLong(line));
                if (block.size() == blockSize) {
                    Collections.sort(block);
                    sortedBlocks.add(saveBlock(block));
                    block.clear();
                }
            }
        }
        if (!block.isEmpty()) {
            Collections.sort(block);
            sortedBlocks.add(saveBlock(block));
        }
        return sortedBlocks;
    }

    /**
     * Метод используется для сохранения блока с данными во временный файл
     *
     * @param block - данные
     * @return - возвращает {@link File} с данными
     * @throws IOException - может выбросить {@link IOException}
     */
    private File saveBlock(List<Long> block) throws IOException {
        File file = File.createTempFile("java_tmp", ".tmp");
        file.deleteOnExit();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Long number : block) {
                writer.write(number.toString());
                writer.newLine();
            }
        }
        return file;
    }

    /**
     * Метод используется для слияния отсортированных блоков в один отсортированный файл
     *
     * @param sortedBlocks - сортированные блоки
     * @param outputFile   - конечный файл
     * @throws IOException - может выбросить {@link IOException}
     */
    public void mergeSortedFiles(List<File> sortedBlocks, File outputFile) throws IOException {
        List<Scanner> scanners = new ArrayList<>();
        PriorityQueue<Long> queue = new PriorityQueue<>();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (File block : sortedBlocks) {
                scanners.add(new Scanner(block));
            }
            Long[] nums = new Long[sortedBlocks.size()];
            for (int i = 0; i < sortedBlocks.size(); i++) {
                Scanner scanner = scanners.get(i);
                String line = scanner.nextLine();
                if (line != null) {
                    nums[i] = Long.parseLong(line);
                    queue.add(nums[i]);
                }
            }
            while (!queue.isEmpty()) {
                Long min = queue.poll();
                writer.write(min + "\n");
                int index = -1;
                for (int i = 0; i < sortedBlocks.size(); i++) {
                    if (nums[i] != null && nums[i].equals(min)) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    Scanner scanner = scanners.get(index);
                    String line = null;
                    if (scanner.hasNext()) {
                        line = scanner.nextLine();
                    }
                    if (line != null) {
                        nums[index] = Long.parseLong(line);
                        queue.add(nums[index]);
                    } else {
                        nums[index] = null;
                    }
                }
            }
            for (Scanner scanner : scanners) {
                scanner.close();
            }
        }
    }
}


