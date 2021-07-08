package expression.generic.setting;

public class ShortAdapter implements TypeAdapter<Short> {

    @Override
    public Short add(Short a, Short b) {
        return (short) (a + b);
    }

    @Override
    public Short subtract(Short a, Short b) {
        return (short) (a - b);
    }

    @Override
    public Short multiply(Short a, Short b) {
        return (short) (a * b);
    }

    @Override
    public Short divide(Short a, Short b) {
        return (short) (a / b);
    }

    @Override
    public Short mod(Short a, Short b) {
        return (short) (a % b);
    }


    @Override
    public Short negate(Short a) {
        return (short) (-a);
    }

    @Override
    public Short abs(Short a) {
        return (short) (a > 0 ? a : -a);
    }

    @Override
    public Short square(Short a) {
        return (short) (a * a);
    }

    @Override
    public Short parseFromInt(int n) {
        return (short) n;
    }

    @Override
    public Short parseFromString(String s) {
        return Short.parseShort(s);
    }
}
