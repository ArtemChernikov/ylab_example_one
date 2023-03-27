package ru.ylab.exampleone.chernikov.lesson04.eventsourcing.db;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 27.03.2023
 */
public interface DataProcessor {

    void getMessage();

    void deletePerson(Long personId);

    void savePerson(Long personId, String firstName, String lastName, String middleName);
}
