package ru.ylab.exampleone.chernikov.lesson05.eventsourcing.api.processor;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 01.04.2023
 */
public interface MQProcessor {
    void sendMessage(String message, String routingKey);
}
