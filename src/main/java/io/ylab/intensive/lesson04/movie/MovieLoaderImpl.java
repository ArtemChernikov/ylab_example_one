package io.ylab.intensive.lesson04.movie;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

public class MovieLoaderImpl implements MovieLoader {

    /**
     * Поле для создания соединения с БД
     */
    private final DataSource dataSource;

    public MovieLoaderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Метод используется для записи данных из csv файла в БД
     *
     * @param file - файл
     */
    @Override
    public void loadData(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             Connection connection = dataSource.getConnection()) {
            reader.readLine();
            reader.readLine();
            String movieData;
            while ((movieData = reader.readLine()) != null) {
                Movie movie = createMovie(movieData);
                saveMovie(connection, movie);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод используется для записи фильма в БД
     *
     * @param connection - соединение с БД
     * @param movie      - фильм для записи в БД
     * @throws SQLException - может выбросить {@link SQLException}
     */
    private void saveMovie(Connection connection, Movie movie) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                """
                        INSERT INTO movie (year, length, title, subject, actors, actress, director, popularity, awards)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
                        """
        )) {
            setMovie(preparedStatement, movie);
            preparedStatement.execute();
        }
    }

    /**
     * Метод используется для загрузки данных из модели фильма в {@link PreparedStatement}
     * (если значения поля фильма null, то записываем в БД null)
     *
     * @param preparedStatement - {@link PreparedStatement}
     * @param movie             - {@link Movie}
     * @throws SQLException - может выбросить {@link SQLException}
     */
    private void setMovie(PreparedStatement preparedStatement, Movie movie) throws SQLException {
        Object[] fieldsArray = getMovieData(movie);
        int[] typesArray = getTypes();
        for (int i = 0; i < fieldsArray.length; i++) {
            Object field = fieldsArray[i];
            if (field == null) {
                preparedStatement.setNull(i + 1, typesArray[i]);
            } else {
                preparedStatement.setObject(i + 1, field, typesArray[i]);
            }
        }
    }

    /**
     * Метод используется для парсинга строки из csv файла в модель фильма {@link Movie}
     *
     * @param movieData - строка с данными о фильме
     * @return - возвращает модель фильма с данными из строки
     */
    private Movie createMovie(String movieData) {
        Movie movie = new Movie();
        try (Scanner scanner = new Scanner(movieData).useDelimiter(MovieLoaderConstants.DELIMITER)) {
            int index = 0;
            while (index < MovieLoaderConstants.IMAGE_INDEX) {
                String data = scanner.next();
                if (!data.isEmpty()) {
                    switch (index) {
                        case MovieLoaderConstants.YEAR_INDEX -> movie.setYear(Integer.parseInt(data));
                        case MovieLoaderConstants.LENGTH_INDEX -> movie.setLength(Integer.parseInt(data));
                        case MovieLoaderConstants.TITLE_INDEX -> movie.setTitle(data);
                        case MovieLoaderConstants.SUBJECT_INDEX -> movie.setSubject(data);
                        case MovieLoaderConstants.ACTOR_INDEX -> movie.setActors(data);
                        case MovieLoaderConstants.ACTRESS_INDEX -> movie.setActress(data);
                        case MovieLoaderConstants.DIRECTOR_INDEX -> movie.setDirector(data);
                        case MovieLoaderConstants.POPULARITY_INDEX -> movie.setPopularity(Integer.parseInt(data));
                        case MovieLoaderConstants.AWARDS_INDEX -> movie.setAwards("Yes".equalsIgnoreCase(data));
                        default -> throw new IllegalStateException("Unexpected value: " + index);
                    }
                }
                index++;
            }
        }
        return movie;
    }

    /**
     * Метод используется для получения свойств фильма
     *
     * @param movie - фильм
     * @return - возвращает массив со свойствами фильма
     */
    private Object[] getMovieData(Movie movie) {
        return new Object[]{
                movie.getYear(),
                movie.getLength(),
                movie.getTitle(),
                movie.getSubject(),
                movie.getActors(),
                movie.getActress(),
                movie.getDirector(),
                movie.getPopularity(),
                movie.getAwards()};
    }

    /**
     * Метод используется для получения типов БД в определенном порядке (порядок полей объекта {@link Movie})
     *
     * @return - возвращает массив с типами
     */
    private int[] getTypes() {
        return new int[]{
                Types.INTEGER,
                Types.INTEGER,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.BOOLEAN};
    }
}
