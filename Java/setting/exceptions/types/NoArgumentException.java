package expression.generic.setting.exceptions.types;

import expression.generic.setting.parser.ParseException;

public class NoArgumentException extends ParseException {

    public NoArgumentException(int point, boolean first) {
        super((first ? "First" : "Second") + " argument expected at: " + point);
    }
}
