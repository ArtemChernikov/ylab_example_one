package ru.ylab.exampleone.chernikov.lesson05.messagefilter.processor;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 02.04.2023
 */
public interface DatabaseProcessor {
    void initDatabase();
    boolean checkWord(String word);
}
