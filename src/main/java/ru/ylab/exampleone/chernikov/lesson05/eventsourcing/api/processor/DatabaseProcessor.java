package ru.ylab.exampleone.chernikov.lesson05.eventsourcing.api.processor;

import ru.ylab.exampleone.chernikov.lesson05.eventsourcing.Person;

import java.util.List;
import java.util.Optional;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 01.04.2023
 */
public interface DatabaseProcessor {
    Optional<Person> findById(Long personId);

    List<Person> findAll();
}
