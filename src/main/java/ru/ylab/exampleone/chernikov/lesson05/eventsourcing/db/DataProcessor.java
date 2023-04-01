package ru.ylab.exampleone.chernikov.lesson05.eventsourcing.db;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 01.04.2023
 */
public interface DataProcessor {
    void startListening();

    void deletePerson(Long personId);

    void savePerson(Long personId, String firstName, String lastName, String middleName);
}
