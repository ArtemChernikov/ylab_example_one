package ru.ylab.exampleone.chernikov.taskthree.password_validator;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 19.03.2023
 */
public class PasswordValidatorTest {
    public static void main(String[] args) {
        System.out.println(PasswordValidator.passwordValidate("", "", ""));
        System.out.println(PasswordValidator.passwordValidate("dsa32aA_", "1234", "1234"));
        System.out.println(PasswordValidator.passwordValidate("asdasfasfafafafafdada", "123", "123"));
        System.out.println(PasswordValidator.passwordValidate("asd", "1111111111111111111111", "1111111111111111111111"));
        System.out.println(PasswordValidator.passwordValidate(".", "123", "123"));
        System.out.println(PasswordValidator.passwordValidate("adas", ",", ","));
        System.out.println(PasswordValidator.passwordValidate("asd", "123", "122"));
    }
}
