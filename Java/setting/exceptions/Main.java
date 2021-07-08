package expression.generic.setting.exceptions;

import expression.generic.setting.Expression;
import expression.generic.setting.IntegerCheckedAdapter;

public class Main {

    public static void main(String[] args) {
        final String expression = "1000000 * x * x * x * x * x / (x - 1)";

        Expression<Integer> result = new ExpressionParser<>(new IntegerCheckedAdapter()).parse(expression);

        System.out.println("x\t\tf");
        for (int i = 0; i <= 10; i++) {
            System.out.print(i + "\t\t");
            try {
                System.out.println(result.evaluate(i, 0, 0));
            } catch (ExpressionException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
