package io.ylab.intensive.tasktwo.snils_validator;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 12.03.2023
 */
public class SnilsValidatorTest {
    public static void main(String[] args) {
        SnilsValidator snilsValidator = new SnilsValidatorImpl();
        System.out.println(snilsValidator.validate(""));
        System.out.println(snilsValidator.validate("           "));
        System.out.println(snilsValidator.validate("asda464623213"));
        System.out.println(snilsValidator.validate(null));
        System.out.println(snilsValidator.validate("1"));
        System.out.println(snilsValidator.validate("01468870570"));
        System.out.println(snilsValidator.validate("90114404441"));
        System.out.println(snilsValidator.validate("02229742636"));
    }
}
