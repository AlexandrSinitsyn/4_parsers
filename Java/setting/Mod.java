package expression.generic.setting;

public class Mod<T extends Number> extends Operation<T> {

    public Mod(Expression<T> e1, Expression<T> e2, TypeAdapter<T> adapter) {
        super(e1, e2, adapter, "mod");
    }


    @Override
    public int getPriority() {
        return GlobalPriorities.HIGH;
    }


    @Override
    protected T evaluate(T x, T y) {
        return adapter.mod(x, y);
    }
}
