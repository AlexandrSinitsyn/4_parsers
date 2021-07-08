package expression.generic.setting;

public class Abs<T extends Number> extends Unary<T>  {

    public Abs(Expression<T> e, TypeAdapter<T> adapter) {
        super(e, adapter, "abs");
    }


    @Override
    protected T evaluate(T x) {
        return adapter.abs(x);
    }
}
