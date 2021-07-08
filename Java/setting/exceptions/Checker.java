package expression.generic.setting.exceptions;

import expression.generic.setting.exceptions.types.OverflowException;

public class Checker {

    public static void checkAdd(int x, int y) {
        if ((0 < y && Integer.MAX_VALUE - y < x) ||
                (y < 0 && x < Integer.MIN_VALUE - y)) {
            throw new OverflowException();
        }
    }

    public static void checkSubtract(int x, int y) {
        if ((0 < y && x < Integer.MIN_VALUE + y) ||
                (y < 0 && Integer.MAX_VALUE + y < x)) {
            throw new OverflowException();
        }
    }

    public static void checkMultiply(int x, int y) {
        if (x == 0 || y == 0) {
            return;
        }

        int result = x * y;
        if (result / x != y || result / y != x) {
            throw new OverflowException();
        }
    }

    public static void checkDivide(int x, int y) {
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new OverflowException();
        }
    }
}
