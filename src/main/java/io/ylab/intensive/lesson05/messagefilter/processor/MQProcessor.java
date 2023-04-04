package io.ylab.intensive.lesson05.messagefilter.processor;

import java.util.Optional;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 02.04.2023
 */
public interface MQProcessor {
    void sendMessage(String message);

    Optional<String> getMessage();
}
