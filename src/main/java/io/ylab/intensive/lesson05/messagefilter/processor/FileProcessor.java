package io.ylab.intensive.lesson05.messagefilter.processor;

import java.util.List;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 02.04.2023
 */
public interface FileProcessor {
    List<String> readFile();
}
