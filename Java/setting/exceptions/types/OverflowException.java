package expression.generic.setting.exceptions.types;

import expression.generic.setting.exceptions.ExpressionException;

public class OverflowException extends ExpressionException {

    public OverflowException() {
        super("Overflow");
    }
}