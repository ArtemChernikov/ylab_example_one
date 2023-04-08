package io.ylab.intensive.taskthree.password_validator;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 19.03.2023
 */
public class WrongPasswordException extends Exception {
    public WrongPasswordException() {
    }

    public WrongPasswordException(String message) {
        super(message);
    }
}
