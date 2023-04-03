package ru.ylab.exampleone.chernikov.lesson05.messagefilter.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ylab.exampleone.chernikov.lesson05.messagefilter.connectionwrapper.DatabaseConnectionWrapper;

import java.sql.*;
import java.util.List;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 02.04.2023
 */
@Component
public class DatabaseProcessorImpl implements DatabaseProcessor {
    /**
     * Поле логгер
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseProcessorImpl.class);

    /**
     * Поле соединение с БД
     */
    private final DatabaseConnectionWrapper databaseConnectionWrapper;

    /**
     * Поле для работы с файлами
     */
    private final FileProcessor fileProcessor;

    @Autowired
    public DatabaseProcessorImpl(DatabaseConnectionWrapper databaseConnectionWrapper, FileProcessor fileProcessor) {
        this.databaseConnectionWrapper = databaseConnectionWrapper;
        this.fileProcessor = fileProcessor;
    }

    /**
     * Метод используется для инициальзации БД
     * 1. При каждом запуске если таблица несуществует, она будет создана
     * 2. При каждом запуске таблица будет очищена от данных
     * 3. При каждом запуске таблица заполняется данными
     */
    @Override
    public void initDatabase() {
        try {
            if (!existTable()) {
                createTable();
            }
            clearTable();
            fillTable();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Метод используется для проверки существования записи в БД без учета регистра
     *
     * @param word - искомое слово
     * @return - возвращает true, если слово существует, если иначе false
     */
    @Override
    public boolean checkWord(String word) {
        boolean exists = false;
        try (PreparedStatement preparedStatement = databaseConnectionWrapper.getConnection().prepareStatement(
                "SELECT * FROM swear WHERE LOWER (word) = LOWER (?);"
        )) {
            preparedStatement.setString(1, word);
            ResultSet resultSet = preparedStatement.executeQuery();
            exists = resultSet.next();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return exists;
    }

    /**
     * Метод используется для создания таблицы в БД
     *
     * @throws SQLException - может выбросить {@link SQLException}
     */
    private void createTable() throws SQLException {
        try (Statement statement = databaseConnectionWrapper.getConnection().createStatement()) {
            statement.execute("""
                    create table swear (
                    word_id serial primary key,
                    word varchar
                    );""");
        }
    }

    /**
     * Метод используется для заполнения таблицы данными (словами)
     *
     * @throws SQLException - может выбросить {@link SQLException}
     */
    private void fillTable() throws SQLException {
        try (PreparedStatement preparedStatement = databaseConnectionWrapper.getConnection().prepareStatement(
                "INSERT INTO swear(word) VALUES(?);"
        )) {
            List<String> words = fileProcessor.readFile();
            for (String word : words) {
                preparedStatement.setString(1, word);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    /**
     * Метод используется для очистки таблицы от данных
     *
     * @throws SQLException - может выбросить {@link SQLException}
     */
    private void clearTable() throws SQLException {
        try (Statement statement = databaseConnectionWrapper.getConnection().createStatement()) {
            statement.execute("TRUNCATE TABLE swear;");
        }
    }

    /**
     * Метод используется для проверки существования таблицы в БД
     *
     * @return - возвращает true, если таблица сущетсвет и false если иначе
     * @throws SQLException - может выбросить {@link SQLException}
     */
    private boolean existTable() throws SQLException {
        boolean exist;
        DatabaseMetaData metaData = databaseConnectionWrapper.getConnection().getMetaData();
        try (ResultSet table = metaData.getTables(null, null, "swear", new String[]{"TABLE"})) {
            exist = table.next();
        }
        return exist;
    }
}
