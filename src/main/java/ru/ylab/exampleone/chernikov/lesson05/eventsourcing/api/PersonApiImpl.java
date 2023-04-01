package ru.ylab.exampleone.chernikov.lesson05.eventsourcing.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ylab.exampleone.chernikov.lesson05.eventsourcing.Person;
import ru.ylab.exampleone.chernikov.lesson05.eventsourcing.api.processor.DatabaseProcessor;
import ru.ylab.exampleone.chernikov.lesson05.eventsourcing.api.processor.MQProcessor;
import ru.ylab.exampleone.chernikov.lesson05.eventsourcing.api.processor.MQProcessorImpl;

import java.util.List;
import java.util.Optional;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 01.04.2023
 */
@Component
public class PersonApiImpl implements PersonApi {
    /**
     * Поле логгер
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonApiImpl.class);
    /**
     * Поле для получения соединения с БД
     */
    private final DatabaseProcessor databaseProcessor;
    /**
     * Поле для работы с RabbitMQ
     */
    private final MQProcessor mqProcessor;

    @Autowired
    public PersonApiImpl(DatabaseProcessor databaseProcessor, MQProcessor mqProcessor) {
        this.databaseProcessor = databaseProcessor;
        this.mqProcessor = mqProcessor;
    }

    /**
     * Метод используется для отправки сообщения-команды по удалению персоны из БД по id
     *
     * @param personId - id персоны для удаления
     */
    @Override
    public void deletePerson(Long personId) {
        if (personId != null && personId > 0) {
            String message = "Delete:" + personId;
            mqProcessor.sendMessage(message, MQProcessorImpl.DELETE_ROUTING_KEY);
        } else {
            LOGGER.error("Введен некорректный id");
        }
    }

    /**
     * Метод используется для отправки сообщения-команды по сохранению в БД новой персоны
     *
     * @param personId   - id
     * @param firstName  - имя
     * @param lastName   - фамилия
     * @param middleName - отчество
     */
    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName) {
        if (personId != null && personId > 0 && firstName != null && lastName != null && middleName != null) {
            String message = "Save:" + personId + ":" + firstName + ":" + lastName + ":" + middleName;
            mqProcessor.sendMessage(message, MQProcessorImpl.SAVE_ROUTING_KEY);
        } else {
            LOGGER.error("Введены некорректные данные");
        }
    }

    /**
     * Метод используется для поиска персоны по id в БД (запрос производится напрямую в БД)
     *
     * @param personId - id
     * @return - возвращает {@link Person} если она существует, иначе null
     */
    @Override
    public Person findPerson(Long personId) {
        if (personId == null || personId <= 0) {
            LOGGER.error("Введен некорректный id");
            return null;
        }
        Optional<Person> foundPerson = databaseProcessor.findById(personId);
        return foundPerson.orElse(null);
    }

    /**
     * Метод используется для поиска всех персон в БД (запрос производится напрямую в БД)
     *
     * @return - возвращает список всех персон
     */
    @Override
    public List<Person> findAll() {
        return databaseProcessor.findAll();
    }
}
