package expression.generic.setting.parser;

import expression.generic.setting.Expression;
import expression.generic.setting.TypeAdapter;

public interface UnaryConstructorCreator<T extends Number> {

    Expression<T> apply (Expression<T> e2, TypeAdapter<T> a);
}
