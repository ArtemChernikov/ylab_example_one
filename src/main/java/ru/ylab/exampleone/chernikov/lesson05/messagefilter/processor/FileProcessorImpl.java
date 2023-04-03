package ru.ylab.exampleone.chernikov.lesson05.messagefilter.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 02.04.2023
 */
@Component
public class FileProcessorImpl implements FileProcessor {
    /**
     * Поле логгер
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessorImpl.class);

    /**
     * Поле путь к файлу со словами
     */
    private static final String PATH = "src/main/resources/messagefilter/swear_words.txt";

    /**
     * Метод используется для чтения слов из файла в список
     *
     * @return - возвращает список слов
     */
    @Override
    public List<String> readFile() {
        File file = new File(PATH);
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                words.add(line);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return words;
    }
}
