package expression.generic.setting;

import java.util.Objects;

public class Variable<T extends Number> implements Expression<T> {

    private final String name;

    public Variable(String name) {
        this.name = name;
    }


    @Override
    public int getPriority() {
        return GlobalPriorities.BASE;
    }


    @Override
    public T evaluate(T x, T y, T z) {
        switch (this.name) {
            case "x": return x;
            case "y": return y;
            case "z": return z;
            default: return null;
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Variable)) {
            return false;
        }

        Variable variable = (Variable) obj;

        return name.equals(variable.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


    @Override
    public String toString() {
        return this.name;
    }
}
