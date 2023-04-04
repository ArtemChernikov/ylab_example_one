package io.ylab.intensive.lesson05.eventsourcing.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.ylab.intensive.lesson05.eventsourcing.db.processor.DatabaseProcessor;
import io.ylab.intensive.lesson05.eventsourcing.db.processor.MQProcessor;

import java.util.Optional;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 01.04.2023
 */
@Component
public class DataProcessorImpl implements DataProcessor {
    /**
     * Поле для работы с RabbitMQ
     */
    private final MQProcessor mqProcessor;
    /**
     * Поле для работы с БД
     */
    private final DatabaseProcessor databaseProcessor;

    @Autowired
    public DataProcessorImpl(MQProcessor mqProcessor, DatabaseProcessor databaseProcessor) {
        this.mqProcessor = mqProcessor;
        this.databaseProcessor = databaseProcessor;
    }

    /**
     * Метод используется для запуска цикла прослушивания очереди (получения сообщений из очереди)
     * и дальнейших выполнений команд исходя из содержания сообщений
     */
    @Override
    public void startListening() {
        while (!Thread.currentThread().isInterrupted()) {
            Optional<String> message = mqProcessor.getMessage();
            message.ifPresent(this::selectAction);
        }
    }

    /**
     * Метод используется для удаления персоны по id из БД
     *
     * @param personId - id персоны
     */
    @Override
    public void deletePerson(Long personId) {
        databaseProcessor.deleteById(personId);
    }

    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName) {
        databaseProcessor.save(personId, firstName, lastName, middleName);
    }

    /**
     * Метод используется для парсинга сообщения из очереди и выполнения соответствующих инструкций
     *
     * @param message - сообщение
     */
    private void selectAction(String message) {
        if (message.startsWith("Delete")) {
            long deleteId = Long.parseLong((message.split(":"))[1]);
            deletePerson(deleteId);
        } else if (message.startsWith("Save")) {
            String[] personData = message.split(":");
            savePerson(Long.parseLong(personData[1]), personData[2], personData[3], personData[4]);
        }
    }
}
