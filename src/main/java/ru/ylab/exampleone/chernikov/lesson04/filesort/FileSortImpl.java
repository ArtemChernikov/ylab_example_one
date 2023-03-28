package ru.ylab.exampleone.chernikov.lesson04.filesort;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileSortImpl implements FileSorter {
    /**
     * Поле для создания соединения с БД
     */
    private final DataSource dataSource;

    public FileSortImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Метод используется для сортировки файла в порядке убывания
     *
     * @param data - неотсортированный файл
     * @return - возвращает новый отсортированный файл
     */
    @Override
    public File sort(File data) {
        File sortedData = new File("src/main/resources/file_sort/sorted_data.txt");
        try (Connection connection = dataSource.getConnection()) {
            readAndSaveNumbers(connection, data);
            getAndWriteSortNumbers(connection, sortedData);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return sortedData;
    }

    /**
     * Метод используется для получения всех чисел из БД в порядке убывания
     * и дальнейшей их записи в файл, также в порядке убывания
     *
     * @param connection - соединение
     * @param sortedData - файл для записи чисел
     * @throws IOException  - может выбросить {@link IOException}
     * @throws SQLException - может выбросить {@link SQLException}
     */
    private void getAndWriteSortNumbers(Connection connection, File sortedData) throws IOException, SQLException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sortedData));
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM numbers ORDER BY val DESC;")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                writer.write(resultSet.getString("val"));
                writer.newLine();
            }
        }
    }

    /**
     * Метод используется для чтения чисел из файла и дальнейшей их записи в БД
     *
     * @param connection - соединение
     * @param data       - файл с числами для чтения
     * @throws IOException  - может выбросить {@link IOException}
     * @throws SQLException - может выбросить {@link SQLException}
     */
    private void readAndSaveNumbers(Connection connection, File data) throws IOException, SQLException {
        try (BufferedReader reader = new BufferedReader(new FileReader(data));
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO numbers VALUES (?);")) {
            String line;
            while ((line = reader.readLine()) != null) {
                preparedStatement.setLong(1, Long.parseLong(line));
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }
}
