package io.ylab.intensive.lesson04.eventsourcing.db;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.ylab.intensive.lesson04.eventsourcing.Person;
import io.ylab.intensive.lesson04.eventsourcing.api.PersonApiImpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 27.03.2023
 */
public class DataProcessorImpl implements DataProcessor {
    /**
     * Поле логгер
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonApiImpl.class);
    /**
     * Поле название обменника
     */
    public static final String EXCHANGE_NAME = "exchange";
    /**
     * Поле название очереди
     */
    public static final String QUEUE_NAME = "queue";
    /**
     * Поле название ключа маршрутизации для сохранения персоны
     */
    public static final String SAVE_ROUTING_KEY = "save_key";
    /**
     * Поле название ключа маршрутизации для удаления персоны
     */
    public static final String DELETE_ROUTING_KEY = "delete_key";
    /**
     * Поле соединение с БД
     */
    private final java.sql.Connection databaseConnection;
    /**
     * Поле соединение с RabbitMQ
     */
    private final Connection mqConnection;

    public DataProcessorImpl(java.sql.Connection databaseConnection, Connection mqConnection) {
        this.databaseConnection = databaseConnection;
        this.mqConnection = mqConnection;
    }

    /**
     * Метод используется для чтения и выполнения сообщений-команд из очереди
     */
    public void getMessage() {
        try (Channel channel = mqConnection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, SAVE_ROUTING_KEY);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, DELETE_ROUTING_KEY);
            while (!Thread.currentThread().isInterrupted()) {
                GetResponse response = channel.basicGet(QUEUE_NAME, false);
                if (response != null) {
                    byte[] body = response.getBody();
                    String message = new String(body, StandardCharsets.UTF_8);
                    selectAction(message);
                    long deliveryTag = response.getEnvelope().getDeliveryTag();
                    channel.basicAck(deliveryTag, false);
                }
            }
        } catch (IOException | TimeoutException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Метод используется для удаления персоны в БД по id
     *
     * @param personId - id
     */
    @Override
    public void deletePerson(Long personId) {
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(
                "DELETE FROM person WHERE person_id = ?;")) {
            preparedStatement.setLong(1, personId);
            int isDeleted = preparedStatement.executeUpdate();
            if (isDeleted == 0) {
                LOGGER.info("Была попытка удаления, персона с данным id не найдена");
            } else {
                LOGGER.info("Удаление успешно");
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Метод используется для сохранения или обновления персоны в БД
     * (сохранение происходит если id персоны нет в БД, обновление если есть)
     *
     * @param personId   - id
     * @param firstName  - имя
     * @param lastName   - фамилия
     * @param middleName - отчество
     */
    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName) {
        try {
            Person person = new Person(personId, firstName, lastName, middleName);
            if (personExists(personId)) {
                update(person);
            } else {
                save(person);
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Метод используется для обновления данных о персоне в БД
     *
     * @param person - новые данные о персоне
     * @throws SQLException - может выбросить {@link SQLException}
     */
    private void update(Person person) throws SQLException {
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(
                "UPDATE person SET first_name = ?, last_name = ?, middle_name = ? WHERE person_id = ?;"
        )) {
            preparedStatement.setString(1, person.getName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setString(3, person.getMiddleName());
            preparedStatement.setLong(4, person.getId());
            preparedStatement.executeUpdate();
            LOGGER.info("Обновление успешно");
        }
    }

    /**
     * Метод испоьзуется для сохранения новой персоны в БД
     *
     * @param person - новая персона
     * @throws SQLException - может выбросить {@link SQLException}
     */
    private void save(Person person) throws SQLException {
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(
                "INSERT INTO person VALUES (?, ?, ?, ?);"
        )) {
            preparedStatement.setLong(1, person.getId());
            preparedStatement.setString(2, person.getName());
            preparedStatement.setString(3, person.getLastName());
            preparedStatement.setString(4, person.getMiddleName());
            preparedStatement.execute();
            LOGGER.info("Сохранение успешно");
        }
    }

    /**
     * Метод используется для проверки есть ли запись в БД по id
     *
     * @param id - id
     * @return - возвращает true если запись существет, иначе false
     * @throws SQLException - может выбросить {@link SQLException}
     */
    private boolean personExists(long id) throws SQLException {
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(
                "SELECT FROM person WHERE person_id = ?;"
        )) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        }
        return false;
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
