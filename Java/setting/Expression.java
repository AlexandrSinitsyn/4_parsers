package expression.generic.setting;

public interface Expression<T extends Number> extends Priority {

    T evaluate(T x, T y, T z);
}
