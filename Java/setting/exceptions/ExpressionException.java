package expression.generic.setting.exceptions;

public abstract class ExpressionException extends ArithmeticException {

    public ExpressionException(final String message) {
        super(message);
    }

    public ExpressionException(String message, int point) {
        super(message + point);
    }
}
