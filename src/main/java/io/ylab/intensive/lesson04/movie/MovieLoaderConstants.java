package io.ylab.intensive.lesson04.movie;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 29.03.2023
 */
public class MovieLoaderConstants {
    /**
     * Поле первый столбец csv файла (year)
     */
    public static final int YEAR_INDEX = 0;
    /**
     * Поле второй столбец csv файла (length)
     */
    public static final int LENGTH_INDEX = 1;
    /**
     * Поле третий столбец csv файла (title)
     */
    public static final int TITLE_INDEX = 2;
    /**
     * Поле четвертый столбец csv файла (subject)
     */
    public static final int SUBJECT_INDEX = 3;
    /**
     * Поле пятый столбец csv файла (actor)
     */
    public static final int ACTOR_INDEX = 4;
    /**
     * Поле шестой столбец csv файла (actress)
     */
    public static final int ACTRESS_INDEX = 5;
    /**
     * Поле седьмой столбец csv файла (director)
     */
    public static final int DIRECTOR_INDEX = 6;
    /**
     * Поле восьмой столбец csv файла (popularity)
     */
    public static final int POPULARITY_INDEX = 7;
    /**
     * Поле девятый столбец csv файла (awards)
     */
    public static final int AWARDS_INDEX = 8;
    /**
     * Поле последний столбец csv файла (image), его мы игнорируем
     */
    public static final int IMAGE_INDEX = 9;

    /**
     * Поле разделитель данных в csv файле
     */
    public static final String DELIMITER = ";";
}
