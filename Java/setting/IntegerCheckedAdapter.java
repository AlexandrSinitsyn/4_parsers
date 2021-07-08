package expression.generic.setting;

import expression.generic.setting.exceptions.Checker;
import expression.generic.setting.exceptions.types.DivisionByZeroException;

public class IntegerCheckedAdapter extends IntegerUncheckedAdapter {

    @Override
    public Integer add(Integer a, Integer b) {
        Checker.checkAdd(a, b);

        return a + b;
    }

    @Override
    public Integer subtract(Integer a, Integer b) {
        Checker.checkSubtract(a, b);

        return a - b;
    }

    @Override
    public Integer multiply(Integer a, Integer b) {
        Checker.checkMultiply(a, b);

        return a * b;
    }

    @Override
    public Integer divide(Integer a, Integer b) {
        DivisionByZeroException.check(a, b);

        Checker.checkDivide(a, b);

        return a / b;
    }

    @Override
    public Integer negate(Integer a) {
        Checker.checkSubtract(0, a);

        return -a;
    }

    @Override
    public Integer abs(Integer a) {
        if (a < 0) {
            Checker.checkSubtract(0, a);
        }

        return a > 0 ? a : -a;
    }

    @Override
    public Integer square(Integer a) {
        Checker.checkMultiply(a, a);

        return a * a;
    }

    @Override
    public Integer mod(Integer a, Integer b) {
        DivisionByZeroException.check(a, b);

        return a % b;
    }
}
