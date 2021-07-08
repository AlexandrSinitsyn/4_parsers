package expression.generic.setting.parser;

public class OperationConstructor {

    private final int priority;

    public OperationConstructor(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
