package expression.generic.setting.exceptions.types;

import expression.generic.setting.parser.ParseException;

public class UnknownArgumentNameException extends ParseException {

    public UnknownArgumentNameException(char c, int point) {
        super("Unknown argument name `" + c + "` at " + point + ". Use one of these: `x`, `y`, `z`");
    }
}

