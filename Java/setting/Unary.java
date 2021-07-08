package expression.generic.setting;

import java.util.Objects;

public abstract class Unary<T extends Number> implements Expression<T> {

    private final Expression<T> e;
    private final String naming;

    protected final TypeAdapter<T> adapter;


    public Unary(Expression<T> e, TypeAdapter<T> adapter, String naming) {
        this.e = e;
        this.adapter = adapter;
        this.naming = naming;
    }


    @Override
    public int getPriority() {
        return GlobalPriorities.UNARY;
    }


    @Override
    public T evaluate(T x, T y, T z) {
        return evaluate(e.evaluate(x, y, z));
    }


    protected abstract T evaluate(T x);


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Unary)) {
            return false;
        }

        Unary minus = (Unary) obj;

        return e.equals(minus.e);
    }

    @Override
    public int hashCode() {
        return Objects.hash(e);
    }


    @Override
    public String toString() {
        return naming + "(" + e.toString() + ")";
    }
}
