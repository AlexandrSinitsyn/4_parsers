package expression.generic.setting.exceptions.types;

import expression.generic.setting.parser.ParseException;

public class NoBracketException extends ParseException {

    public NoBracketException(int point, boolean opening) {
        super("No " + (opening ? "opening" : "closing") + " bracket found: " + point);
    }
}
