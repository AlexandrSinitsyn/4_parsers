package expression.generic.setting;

public class Negate<T extends Number> extends Unary<T>  {

    public Negate(Expression<T> e, TypeAdapter<T> adapter) {
        super(e, adapter, "-");
    }


    @Override
    protected T evaluate(T x) {
        return adapter.negate(x);
    }
}
