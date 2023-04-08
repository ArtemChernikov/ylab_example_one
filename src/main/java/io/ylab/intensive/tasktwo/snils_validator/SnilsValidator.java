package io.ylab.intensive.tasktwo.snils_validator;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 12.03.2023
 */
public interface SnilsValidator {
    /**
     * Поле общая длина номера СНИЛС
     */
    int SNILS_LENGTH = 11;
    /**
     * Поле длина номера СНИЛС без контрольного числа
     */
    int LENGTH_OF_SNILS_NUMBER = 9;
    /**
     * Поле длина контрольного числа СНИЛС
     */
    int LENGTH_OF_SNILS_CONTROL_NUMBER = 2;

    /**
     * Проверяет, что в строке содержится валидный номер СНИЛС
     *
     * @param snils снилс
     * @return результат проверки
     */
    boolean validate(String snils);
}
