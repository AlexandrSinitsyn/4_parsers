package expression.generic.setting.exceptions.types;

import expression.generic.setting.parser.ParseException;

public class NoSeparatorException extends ParseException {

    public NoSeparatorException(int point) {
        super("No separator between operation and argument found at: " + point);
    }
}
