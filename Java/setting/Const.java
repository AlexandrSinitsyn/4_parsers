package expression.generic.setting;

import java.util.Objects;

public class Const<T extends Number> implements Expression<T> {

    private final T c;

    public Const(T c) {
        this.c = c;
    }


    @Override
    public int getPriority() {
        return GlobalPriorities.BASE;
    }


    @Override
    public T evaluate(T x, T y, T z) {
        return c;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Const)) {
            return false;
        }

        Const aConst = (Const) obj;

        return c == aConst.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(c);
    }


    @Override
    public String toString() {
        return c.toString();
    }
}
