package expression.generic.setting;

public class DoubleAdapter implements TypeAdapter<Double> {

    @Override
    public Double add(Double a, Double b) {
        return a + b;
    }

    @Override
    public Double subtract(Double a, Double b) {
        return a - b;
    }

    @Override
    public Double multiply(Double a, Double b) {
        return a * b;
    }

    @Override
    public Double divide(Double a, Double b) {
        return a / b;
    }


    @Override
    public Double mod(Double a, Double b) {
        return a % b;
    }

    @Override
    public Double negate(Double a) {
        return -a;
    }

    @Override
    public Double abs(Double a) {
        return a > 0 ? a : -a;
    }

    @Override
    public Double square(Double a) {
        return a * a;
    }


    @Override
    public Double parseFromInt(int n) {
        return (double) n;
    }

    @Override
    public Double parseFromString(String s) {
        return Double.parseDouble(s);
    }
}
