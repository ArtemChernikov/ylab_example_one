package io.ylab.intensive.lesson04.persistentmap;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, методы которого надо реализовать
 */
public class PersistentMapImpl implements PersistentMap {

    /**
     * Поле для создания соединений с БД
     */
    private final DataSource dataSource;

    /**
     * Поле имя экземпляра map с которым происходит работа
     */
    private String name;

    public PersistentMapImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Метод используется для инициализации map, с которым будет происходить работа
     *
     * @param name - имя map
     */
    @Override
    public void init(String name) {
        this.name = name;
    }

    /**
     * Метод используется для проверки находится ли данный ключ в БД
     *
     * @param key - ключ
     * @return - возвращает true, если ключ существует, false, если иначе
     * @throws SQLException - может выбросить {@link SQLException}
     */
    @Override
    public boolean containsKey(String key) throws SQLException {
        boolean keyIsNull = key == null;
        String sql = "SELECT * FROM persistent_map WHERE map_name = ? AND key "
                + (keyIsNull ? "IS NULL;" : "= ?;");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            if (!keyIsNull) {
                preparedStatement.setString(2, key);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Метод используется для получения всех ключей определенной map из БД
     *
     * @return - возвращает список ключей
     * @throws SQLException - может выбросить {@link SQLException}
     */
    @Override
    public List<String> getKeys() throws SQLException {
        List<String> keys = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT key FROM persistent_map WHERE map_name = ?;"
             )) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                keys.add(resultSet.getString(1));
            }
        }
        return keys;
    }

    /**
     * Метод используется для получения значения по ключу из БД
     *
     * @param key - ключ
     * @return - возвращает значение по ключу, если ключ сущетсвует, иначе null
     * @throws SQLException - может выбросить {@link SQLException}
     */
    @Override
    public String get(String key) throws SQLException {
        boolean keyIsNull = key == null;
        String sql = "SELECT value FROM persistent_map WHERE map_name = ? AND key "
                + (keyIsNull ? "IS NULL;" : "= ?;");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            if (!keyIsNull) {
                preparedStatement.setString(2, key);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        }
        return null;
    }

    /**
     * Метод используется для удаления из БД пары ключ-значения по ключу определенной map
     *
     * @param key - ключ
     * @throws SQLException - может выбросить {@link SQLException}
     */
    @Override
    public void remove(String key) throws SQLException {
        boolean keyIsNull = key == null;
        String sql = "DELETE FROM persistent_map WHERE map_name = ? AND key "
                + (keyIsNull ? "IS NULL;" : "= ?;");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            if (!keyIsNull) {
                preparedStatement.setString(2, key);
            }
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Метод используется для записи новой пары ключ-значение в БД
     *
     * @param key   - ключ
     * @param value - значение
     * @throws SQLException - может выбросить {@link SQLException}
     */
    @Override
    public void put(String key, String value) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO persistent_map VALUES(?, ?, ?);"
             )) {
            if (containsKey(key)) {
                remove(key);
            }
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, key);
            preparedStatement.setString(3, value);
            preparedStatement.execute();
        }
    }

    /**
     * Метод используется для удаления всех пар ключ-значений определенной map в БД
     *
     * @throws SQLException - может выбросить {@link SQLException}
     */
    @Override
    public void clear() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM persistent_map WHERE map_name = ?;"
             )) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        }
    }
}
