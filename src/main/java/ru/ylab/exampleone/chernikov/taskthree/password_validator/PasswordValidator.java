package ru.ylab.exampleone.chernikov.taskthree.password_validator;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 19.03.2023
 */
public class PasswordValidator {

    public static boolean passwordValidate(String login, String password, String confirmPassword) {
        if (!login.matches("^[a-zA-Z0-9_]*$")) {
            try {
                throw new WrongLoginException("Логин содержит недопустимые символы");
            } catch (WrongLoginException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        if (login.length() >= 20) {
            try {
                throw new WrongLoginException("Логин слишком длинный");
            } catch (WrongLoginException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        if (!password.matches("^[a-zA-Z0-9_]*$")) {
            try {
                throw new WrongPasswordException("Пароль содержит недопустимые символы");
            } catch (WrongPasswordException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        if (password.length() >= 20) {
            try {
                throw new WrongPasswordException("Пароль слишком длинный");
            } catch (WrongPasswordException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        if (!password.equals(confirmPassword)) {
            try {
                throw new WrongPasswordException("Пароль и подтверждение не совпадают");
            } catch (WrongPasswordException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        return true;
    }
}
