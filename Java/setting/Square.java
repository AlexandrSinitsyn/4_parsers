package expression.generic.setting;

public class Square<T extends Number> extends Unary<T>  {

    public Square(Expression<T> e, TypeAdapter<T> adapter) {
        super(e, adapter, "square");
    }


    @Override
    protected T evaluate(T x) {
        return adapter.square(x);
    }
}
