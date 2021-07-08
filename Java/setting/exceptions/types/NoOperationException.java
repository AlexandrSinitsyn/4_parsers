package expression.generic.setting.exceptions.types;

import expression.generic.setting.parser.ParseException;

public class NoOperationException extends ParseException {

    public NoOperationException(int point) {
        super("No matching operation or no space between operation and variable found at: " + point);
    }
}
