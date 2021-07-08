package expression.generic.setting.exceptions.types;

import expression.generic.setting.parser.ParseException;

public class InvalidNumberException extends ParseException {

    public InvalidNumberException(int point) {
        super("Invalid number exception occurred at: " + point);
    }

    public InvalidNumberException(String message) {
        super(message);
    }
}
