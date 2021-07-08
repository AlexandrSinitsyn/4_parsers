package expression.generic.setting;

import java.util.Objects;

public abstract class Operation<T extends Number> implements Expression<T> {

    protected final Expression<T> e1, e2;
    protected final TypeAdapter<T> adapter;
    protected final String operationSymbol;

    public Operation(final Expression<T> e1, final Expression<T> e2, TypeAdapter<T> adapter, final String operationSymbol) {
        this.e1 = e1;
        this.e2 = e2;
        this.adapter = adapter;
        this.operationSymbol = operationSymbol;
    }


    @Override
    public T evaluate(T x, T y, T z) {
        return evaluate(e1.evaluate(x, y, z), e2.evaluate(x, y, z));
    }

    protected abstract T evaluate(T x, T y);


    @Override
    public int hashCode() {
        return Objects.hash(e1.hashCode(), e2.hashCode(), this.getClass().hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Operation)) {
            return false;
        }

        Operation op = (Operation) obj;

        return operationSymbol.equals(op.operationSymbol) &&
                e1.equals(op.e1) &&
                e2.equals(op.e2);
    }


    @Override
    public String toString() {
        return new StringBuilder().
                append("(").
                append(e1.toString()).append(" ").
                append(operationSymbol).append(" ").
                append(e2.toString()).
                append(")").
                toString();
    }
}
