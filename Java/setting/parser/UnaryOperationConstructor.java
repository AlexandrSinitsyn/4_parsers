package expression.generic.setting.parser;

import expression.generic.setting.Priority;

public class UnaryOperationConstructor<T extends Number> extends OperationConstructor {

    private final UnaryConstructorCreator<T> constructor;

    public UnaryOperationConstructor(UnaryConstructorCreator<T> constructor) {
        super(Priority.GlobalPriorities.UNARY);
        this.constructor = constructor;
    }

    public UnaryConstructorCreator<T> getConstructor() {
        return constructor;
    }
}
