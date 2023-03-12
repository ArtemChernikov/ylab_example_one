package ru.ylab.exampleone.chernikov.tasktwo.snils_validator;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 12.03.2023
 */
public class SnilsValidatorImpl implements SnilsValidator {
    @Override
    public boolean validate(String snils) {
        boolean valid = true;
        if (snils == null) {
            valid = false;
        } else if (snils.length() == 0) {
            valid = false;
        } else if (!snils.matches("^[0-9]*$")) {
            valid = false;
        } else if (snils.length() != SNILS_LENGTH) {
            valid = false;
        } else {
            int sum = 0;
            for (int i = 0; i < LENGTH_OF_SNILS_NUMBER; i++) {
                sum += Character.digit(snils.charAt(i), 10) * (LENGTH_OF_SNILS_NUMBER - i);
            }
            int checkControlNumber = 0;
            if (sum < 100) {
                checkControlNumber = sum;
            } else if (sum > 100) {
                checkControlNumber = sum % 101;
                if (checkControlNumber == 100) {
                    checkControlNumber = 0;
                }
            }
            int realControlNumber = Integer.parseInt(snils.substring(LENGTH_OF_SNILS_NUMBER));
            if (checkControlNumber != realControlNumber) {
                valid = false;
            }
        }
        return valid;
    }
}
