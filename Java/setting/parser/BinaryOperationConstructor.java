package expression.generic.setting.parser;

public class BinaryOperationConstructor<T extends Number> extends OperationConstructor {

    private final BinaryConstructorCreator<T> constructor;

    public BinaryOperationConstructor(BinaryConstructorCreator<T> constructor, int priority) {
        super(priority);
        this.constructor = constructor;
    }

    public BinaryConstructorCreator<T> getConstructor() {
        return constructor;
    }
}
