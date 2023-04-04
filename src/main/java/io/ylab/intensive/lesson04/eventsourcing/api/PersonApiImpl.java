package io.ylab.intensive.lesson04.eventsourcing.api;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import io.ylab.intensive.lesson04.eventsourcing.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Тут пишем реализацию
 */
public class PersonApiImpl implements PersonApi {
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


    public PersonApiImpl(java.sql.Connection databaseConnection, Connection mqConnection) {
        this.databaseConnection = databaseConnection;
        this.mqConnection = mqConnection;
    }

    /**
     * Метод используется для отправки сообщения-команды по удалению персоны из БД по id
     *
     * @param personId - id персоны для удаления
     */
    @Override
    public void deletePerson(Long personId) {
        if (personId != null && personId > 0) {
            try (Channel channel = mqConnection.createChannel()) {
                String message = "Delete:" + personId;
                createExchangeAndQueue(channel);
                channel.basicPublish(EXCHANGE_NAME, DELETE_ROUTING_KEY, null, message.getBytes(StandardCharsets.UTF_8));
            } catch (IOException | TimeoutException e) {
                LOGGER.error(e.getMessage(), e);
            }
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
            try (Channel channel = mqConnection.createChannel()) {
                String message = "Save:" + personId + ":" + firstName + ":" + lastName + ":" + middleName;
                createExchangeAndQueue(channel);
                channel.basicPublish(EXCHANGE_NAME, SAVE_ROUTING_KEY, null, message.getBytes(StandardCharsets.UTF_8));
            } catch (IOException | TimeoutException e) {
                LOGGER.error(e.getMessage(), e);
            }
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
        if (personId == null || personId > 0) {
            LOGGER.error("Введен некорректный id");
            return null;
        }
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement("SELECT * FROM person WHERE person_id = ?;")) {
            preparedStatement.setLong(1, personId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return setPerson(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Метод используется для поиска все персон в БД (запрос производится напрямую в БД)
     *
     * @return - возвращает список всех персон
     */
    @Override
    public List<Person> findAll() {
        List<Person> persons = new ArrayList<>();
        try (PreparedStatement preparedStatement = databaseConnection.prepareStatement("SELECT * FROM person;")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                persons.add(setPerson(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return persons;
    }

    /**
     * Метод используется для создания обменника, очереди и связывания их по ключам маршуртизации
     *
     * @param channel - {@link Channel}
     * @throws IOException - может выбросить {@link IOException}
     */
    private void createExchangeAndQueue(Channel channel) throws IOException {
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, DELETE_ROUTING_KEY);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, SAVE_ROUTING_KEY);
    }

    /**
     * Метод используется для создания персоны по данным из {@link ResultSet}
     *
     * @param resultSet - {@link ResultSet}
     * @return - возвращает персону
     * @throws SQLException - может выбросить {@link SQLException}
     */
    private Person setPerson(ResultSet resultSet) throws SQLException {
        return new Person(resultSet.getLong("person_id"), resultSet.getString("first_name"),
                resultSet.getString("last_name"), resultSet.getString("middle_name"));
    }
}
