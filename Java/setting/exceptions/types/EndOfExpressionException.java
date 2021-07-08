package expression.generic.setting.exceptions.types;

import expression.generic.setting.parser.ParseException;

public class EndOfExpressionException extends ParseException {

    public EndOfExpressionException(int point) {
        super("End of expression expected at: " + point);
    }
}
