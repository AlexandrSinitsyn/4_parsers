package expression.generic.setting;

import java.math.BigInteger;

public class BigIntegerAdapter implements TypeAdapter<BigInteger> {

    @Override
    public BigInteger add(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    @Override
    public BigInteger subtract(BigInteger a, BigInteger b) {
        return a.subtract(b);
    }

    @Override
    public BigInteger multiply(BigInteger a, BigInteger b) {
        return a.multiply(b);
    }

    @Override
    public BigInteger divide(BigInteger a, BigInteger b) {
        return a.divide(b);
    }

    @Override
    public BigInteger mod(BigInteger a, BigInteger b) {
        return a.mod(b);
    }


    @Override
    public BigInteger negate(BigInteger a) {
        return a.negate();
    }

    @Override
    public BigInteger abs(BigInteger a) {
        return a.abs();
    }

    @Override
    public BigInteger square(BigInteger a) {
        return a.multiply(a);
    }

    @Override
    public BigInteger parseFromInt(int n) {
        return BigInteger.valueOf(n);
    }

    @Override
    public BigInteger parseFromString(String s) {
        return new BigInteger(s);
    }
}
