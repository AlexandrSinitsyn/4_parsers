package expression.generic;

import expression.generic.setting.*;
import expression.generic.setting.exceptions.ExpressionParser;

import java.util.Map;

public class GenericTabulator implements Tabulator {

    private static final Map<String, TypeAdapter<? extends Number>> modes = Map.of(
            "i", new IntegerCheckedAdapter(),
            "d", new DoubleAdapter(),
            "bi", new BigIntegerAdapter(),
            "u", new IntegerUncheckedAdapter(),
            "l", new LongAdapter(),
            "s", new ShortAdapter()
            );

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        return tabulateByType(modes.get(mode), expression, x1, x2, y1, y2, z1, z2);
    }

    private <T extends Number> Object[][][] tabulateByType(TypeAdapter<T> mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        Object[][][] array = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];

        final Expression<T> parsed = new ExpressionParser<>(mode).parse(expression);

        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    try {
                        final T value = parsed.evaluate(mode.parseFromInt(x), mode.parseFromInt(y), mode.parseFromInt(z));
                        array[x - x1][y - y1][z - z1] = value;
                    } catch (ArithmeticException e) {
                        // do nothing
                    }
                }
            }
        }

        return array;
    }


    public static void main(String[] args) {
        assert args.length == 2;

        assert args[0].startsWith("-");

        final int bottomBound = -2;
        final int topBound = 2;
        final String expression = args[1];
        Object[][][] ans = new GenericTabulator().tabulateByType(modes.get(args[0].substring(1)), expression, bottomBound, topBound, bottomBound, topBound, bottomBound, topBound);

        for (int x = bottomBound; x <= topBound; x++) {
            for (int y = bottomBound; y <= topBound; y++) {
                for (int z = bottomBound; z <= topBound; z++) {
                    System.out.printf("expression=%s\nresult=%s (x=%d y=%d z=%d)\n", expression, ans[x - bottomBound][y - bottomBound][z - bottomBound], x, y, z);
                }
            }
        }
    }
}
