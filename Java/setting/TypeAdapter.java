package expression.generic.setting;

public interface TypeAdapter<T extends Number> {

    T add(T a, T b);

    T subtract(T a, T b);

    T multiply (T a, T b);

    T divide(T a, T b);

    T mod(T a, T b);


    T negate(T a);

    T abs(T a);

    T square(T a);


    T parseFromInt(int n);

    T parseFromString(String s);
}
