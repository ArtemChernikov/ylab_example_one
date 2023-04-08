package io.ylab.intensive.lesson05.eventsourcing.db.processor;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 01.04.2023
 */
public interface DatabaseProcessor {
    void deleteById(Long personId);

    void save(Long personId, String firstName, String lastName, String middleName);
}
