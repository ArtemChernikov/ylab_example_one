package ru.ylab.exampleone.chernikov.lesson04.movie;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

public class MovieLoaderImpl implements MovieLoader {
    /**
     * Поле первый столбец csv файла (year)
     */
    private static final int YEAR_INDEX = 0;
    /**
     * Поле второй столбец csv файла (length)
     */
    private static final int LENGTH_INDEX = 1;
    /**
     * Поле третий столбец csv файла (title)
     */
    private static final int TITLE_INDEX = 2;
    /**
     * Поле четвертый столбец csv файла (subject)
     */
    private static final int SUBJECT_INDEX = 3;
    /**
     * Поле пятый столбец csv файла (actor)
     */
    private static final int ACTOR_INDEX = 4;
    /**
     * Поле шестой столбец csv файла (actress)
     */
    private static final int ACTRESS_INDEX = 5;
    /**
     * Поле седьмой столбец csv файла (director)
     */
    private static final int DIRECTOR_INDEX = 6;
    /**
     * Поле восьмой столбец csv файла (popularity)
     */
    private static final int POPULARITY_INDEX = 7;
    /**
     * Поле девятый столбец csv файла (awards)
     */
    private static final int AWARDS_INDEX = 8;
    /**
     * Поле последний столбец csv файла (image), его мы игнорируем
     */
    private static final int IMAGE_INDEX = 9;

    /**
     * Поле разделитель данных в csv файле
     */
    private static final String DELIMITER = ";";
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
        Object[] fieldsArray = new Object[]{movie.getYear(), movie.getLength(), movie.getTitle(), movie.getSubject(),
                movie.getActors(), movie.getActress(), movie.getDirector(), movie.getPopularity(), movie.getAwards()};
        int[] typesArray = new int[]{Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.BOOLEAN};
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
        try (Scanner scanner = new Scanner(movieData).useDelimiter(DELIMITER)) {
            int index = 0;
            while (index < IMAGE_INDEX) {
                String data = scanner.next();
                if (!data.isEmpty()) {
                    switch (index) {
                        case YEAR_INDEX -> movie.setYear(Integer.parseInt(data));
                        case LENGTH_INDEX -> movie.setLength(Integer.parseInt(data));
                        case TITLE_INDEX -> movie.setTitle(data);
                        case SUBJECT_INDEX -> movie.setSubject(data);
                        case ACTOR_INDEX -> movie.setActors(data);
                        case ACTRESS_INDEX -> movie.setActress(data);
                        case DIRECTOR_INDEX -> movie.setDirector(data);
                        case POPULARITY_INDEX -> movie.setPopularity(Integer.parseInt(data));
                        case AWARDS_INDEX -> movie.setAwards("Yes".equalsIgnoreCase(data));
                        default -> throw new IllegalStateException("Unexpected value: " + index);
                    }
                }
                index++;
            }
        }
        return movie;
    }
}
