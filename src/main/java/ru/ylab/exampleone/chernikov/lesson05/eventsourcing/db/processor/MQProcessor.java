package ru.ylab.exampleone.chernikov.lesson05.eventsourcing.db.processor;

import java.util.Optional;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 01.04.2023
 */
public interface MQProcessor {
    Optional<String> getMessage();
}
