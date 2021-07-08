package expression.generic.setting.exceptions.types;

import expression.generic.setting.exceptions.ExpressionException;

public class DivisionByZeroException extends ExpressionException {

    public DivisionByZeroException() {
        super("Division by zero occurred");
    }


    public static void check(int x, int y) {
        if (y == 0) {
            throw new DivisionByZeroException();
        }
    }
}
