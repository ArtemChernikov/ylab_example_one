package io.ylab.intensive.lesson05.eventsourcing.db.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.ylab.intensive.lesson05.eventsourcing.Person;
import io.ylab.intensive.lesson05.eventsourcing.db.connectionwrapper.DatabaseConnectionWrapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 01.04.2023
 */
@Component
public class DatabaseProcessorImpl implements DatabaseProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseProcessorImpl.class);
    /**
     * Поле для получения соединения с БД
     */
    private final DatabaseConnectionWrapper databaseConnectionWrapper;

    @Autowired
    public DatabaseProcessorImpl(DatabaseConnectionWrapper databaseConnectionWrapper) {
        this.databaseConnectionWrapper = databaseConnectionWrapper;
    }

    /**
     * Метод используется для удаления персоны из БД по id
     *
     * @param personId - id персоны
     */
    @Override
    public void deleteById(Long personId) {
        try (PreparedStatement preparedStatement = databaseConnectionWrapper.getConnection().prepareStatement(
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
     * Метод используется для записи или обновления персоны в БД
     *
     * @param personId   - id
     * @param firstName  - имя
     * @param lastName   - фамилия
     * @param middleName - отчество
     */
    @Override
    public void save(Long personId, String firstName, String lastName, String middleName) {
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
        try (PreparedStatement preparedStatement = databaseConnectionWrapper.getConnection().prepareStatement(
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
        try (PreparedStatement preparedStatement = databaseConnectionWrapper.getConnection().prepareStatement(
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
        try (PreparedStatement preparedStatement = databaseConnectionWrapper.getConnection().prepareStatement(
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
}
