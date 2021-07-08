package expression.generic.setting;

public class Divide<T extends Number> extends Operation<T> {

    public Divide(Expression<T> e1, Expression<T> e2, TypeAdapter<T> adapter) {
        super(e1, e2, adapter, "/");
    }


    @Override
    public int getPriority() {
        return GlobalPriorities.HIGH;
    }


    @Override
    protected T evaluate(T x, T y) {
        return adapter.divide(x, y);
    }
}
