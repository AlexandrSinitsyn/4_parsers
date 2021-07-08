package expression.generic.setting;

public class Add<T extends Number> extends Operation<T> {

    public Add(Expression<T> e1, Expression<T> e2, TypeAdapter<T> adapter) {
        super(e1, e2, adapter, "+");
    }


    @Override
    public int getPriority() {
        return GlobalPriorities.LOW;
    }


    @Override
    protected T evaluate(T x, T y) {
        return adapter.add(x, y);
    }
}
