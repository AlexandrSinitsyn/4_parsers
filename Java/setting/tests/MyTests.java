package expression.generic.setting.tests;

import expression.generic.setting.Add;
import expression.generic.setting.Expression;
import expression.generic.setting.Mod;
import expression.generic.setting.Variable;
import expression.generic.setting.exceptions.ExpressionParser;
import expression.generic.setting.IntegerCheckedAdapter;
import expression.generic.setting.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MyTests {

    private static final IntegerCheckedAdapter adapter = new IntegerCheckedAdapter();
    private static final Variable<Integer> x = new Variable<>("x");
    private static final Variable<Integer> y = new Variable<>("y");

    private Expression<Integer> parse(final String expression) {
        return new ExpressionParser<>(adapter).parse(expression);
    }
    

    private void valid(final Expression<Integer> expected, final String expression) {
        Assertions.assertEquals(expected, parse(expression));
    }

    private void invalid(final String input) {
        try {
            final Object value = parse(input);
            Assertions.fail("Expected fail, found " + value + " for " + input);
        } catch (ParseException e) {
            System.out.println("Expected error");
            System.out.println("    " + input);
            System.out.println("    " + e.getMessage());
        }
    }

    @Test
    public void test() {
        valid(new Add<>(x, y, adapter), "x + y");
        invalid("x + ");
        invalid("+ x");
        invalid("+");
        invalid("x x");
        invalid("x * * y");

        invalid("(");
        invalid(")");
        invalid(")");
        valid(x, "(x)");
        invalid("(x + )");
        invalid("(x))");
        invalid("((x)");
    }

    @Test
    public void mod() {
        valid(new Mod<>(x, y, adapter), "x mod y");
        invalid("1mod1");
        invalid("1mod");
        invalid("mod1");
        invalid("1 mod");
        invalid("1 mod1");
//        invalid("1mod 1");
        invalid("1mod");
        invalid("x mod1");
        invalid("1 modx");
        invalid("1 mod mod 1");
    }

    @Test
    public void parser() {
        Expression<Integer> parse = parse("10 + 4 / 2 - 7");
        System.out.println(parse.toString());
        System.out.println(parse.evaluate(0, 0, 0));
        System.out.println(parse.evaluate(0, -8, -7));
    }
}
