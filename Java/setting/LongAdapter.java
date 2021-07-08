package expression.generic.setting;

public class LongAdapter implements TypeAdapter<Long> {

    @Override
    public Long add(Long a, Long b) {
        return a + b;
    }

    @Override
    public Long subtract(Long a, Long b) {
        return a - b;
    }

    @Override
    public Long multiply(Long a, Long b) {
        return a * b;
    }

    @Override
    public Long divide(Long a, Long b) {
        return a / b;
    }

    @Override
    public Long mod(Long a, Long b) {
        return a % b;
    }


    @Override
    public Long negate(Long a) {
        return -a;
    }

    @Override
    public Long abs(Long a) {
        return a > 0 ? a : -a;
    }

    @Override
    public Long square(Long a) {
        return a * a;
    }

    @Override
    public Long parseFromInt(int n) {
        return (long) n;
    }

    @Override
    public Long parseFromString(String s) {
        return Long.parseLong(s);
    }
}
