package io.ylab.intensive.lesson05.eventsourcing.api.processor;

import io.ylab.intensive.lesson05.eventsourcing.api.connectionwrapper.DatabaseConnectionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.ylab.intensive.lesson05.eventsourcing.Person;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 01.04.2023
 */
@Component
public class DatabaseProcessorImpl implements DatabaseProcessor {
    /**
     * Поле логгер
     */
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
     * Метод используется для поиска персоны в БД по id
     *
     * @param personId - id
     * @return - если персона существует, то возвращает персону обернутую в {@link Optional}, иначе пустой {@link Optional}
     */
    public Optional<Person> findById(Long personId) {
        try (PreparedStatement preparedStatement = databaseConnectionWrapper.getConnection().prepareStatement(
                "SELECT * FROM person WHERE person_id = ?;")) {
            preparedStatement.setLong(1, personId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(setPerson(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    /**
     * Метод используется для поиска всех персон в БД
     *
     * @return - возвращает список персон
     */
    public List<Person> findAll() {
        List<Person> persons = new ArrayList<>();
        try (PreparedStatement preparedStatement = databaseConnectionWrapper.getConnection().prepareStatement(
                "SELECT * FROM person;")) {
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
