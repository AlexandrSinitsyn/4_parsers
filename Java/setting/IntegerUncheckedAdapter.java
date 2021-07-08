package expression.generic.setting;

public class IntegerUncheckedAdapter implements TypeAdapter<Integer> {

    @Override
    public Integer add(Integer a, Integer b) {
        return a + b;
    }

    @Override
    public Integer subtract(Integer a, Integer b) {
        return a - b;
    }

    @Override
    public Integer multiply(Integer a, Integer b) {
        return a * b;
    }

    @Override
    public Integer divide(Integer a, Integer b) {
        return a / b;
    }

    @Override
    public Integer mod(Integer a, Integer b) {
        return a % b;
    }


    @Override
    public Integer negate(Integer a) {
        return -a;
    }

    @Override
    public Integer abs(Integer a) {
        return a > 0 ? a : -a;
    }

    @Override
    public Integer square(Integer a) {
        return a * a;
    }

    @Override
    public Integer parseFromInt(int n) {
        return n;
    }

    @Override
    public Integer parseFromString(String s) {
        return Integer.parseInt(s);
    }
}
