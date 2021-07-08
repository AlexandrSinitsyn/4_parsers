package expression.generic.setting.exceptions;

import expression.generic.setting.*;
import expression.generic.setting.exceptions.types.NoArgumentException;
import expression.generic.setting.TypeAdapter;
import expression.generic.setting.parser.*;

import java.util.Map;

import static expression.generic.setting.Priority.*;

public class ExpressionParser<T extends Number> {

    private final Map<String, BinaryOperationConstructor<T>> binaryOperationConstructors = Map.of(
            "+", new BinaryOperationConstructor<>(Add::new, GlobalPriorities.LOW),
            "-", new BinaryOperationConstructor<>(Subtract::new, GlobalPriorities.LOW),
            "*", new BinaryOperationConstructor<>(Multiply::new, GlobalPriorities.HIGH),
            "/", new BinaryOperationConstructor<>(Divide::new, GlobalPriorities.HIGH),
            "mod", new BinaryOperationConstructor<>(Mod::new, GlobalPriorities.HIGH)
    );

    private final Map<String, UnaryOperationConstructor<T>> unaryOperationConstructors = Map.of(
            "-", new UnaryOperationConstructor<>(Negate::new),
            "abs", new UnaryOperationConstructor<>(Abs::new),
            "square", new UnaryOperationConstructor<>(Square::new)
    );

    private final TypeAdapter<T> adapter;


    public ExpressionParser(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }


    public Expression<T> parse(String source) {
        return new ExpressionAnalysis(new StringSource(source)).parseExpression();
    }


    private class ExpressionAnalysis extends BaseParser {

        public ExpressionAnalysis(final CharSource source) {
            super(source);
            nextChar();
        }


        public Expression<T> parseExpression() {
            Expression<T> result = parseElement(null, GlobalPriorities.BASE);

            if (!eof()) {
                throw expectedEndOfExpressionError();
            }

            return result;
        }

        private Expression<T> parseElement(int lastOperationPriority) {
            return parseElement(null, lastOperationPriority);
        }

        private Expression<T> parseElement(Expression<T> expression, int lastOperationPriority) {
            skipWhitespace();

            if (expression == null) {
                expression = parseSegments();

                skipWhitespace();
            }

            if (eof() || ch == ')') {
                return expression;
            }

            Expression<T> res = parseOperation(expression, lastOperationPriority);

            if (expression.equals(res)) {
                return expression;
            }

            skipWhitespace();

            return parseElement(res, lastOperationPriority);
        }


        private Expression<T> parseOperation(Expression<T> expression, int lastOperationPriority) {
            try {
                markPosition();

                if (!Character.isLetter(ch) && !Character.isDigit(ch) && !Character.isWhitespace(ch)) {
                    final BinaryOperationConstructor<T> oneSymbolOperation = binaryOperationConstructors.getOrDefault(String.valueOf(ch), null);

                    if (oneSymbolOperation != null && lastOperationPriority < oneSymbolOperation.getPriority()) {
                        nextChar();

                        return parseBinaryOperation(expression, oneSymbolOperation);
                    }
                } else {
                    StringBuilder name = new StringBuilder();

                    while (!Character.isWhitespace(ch) && !eof()) {
                        name.append(ch);
                        nextChar();
                    }

                    final BinaryOperationConstructor<T> operation = binaryOperationConstructors.getOrDefault(name.toString(), null);

                    if (operation != null && lastOperationPriority < operation.getPriority()) {
                        return parseBinaryOperation(expression, operation);
                    }
                }

                reset();

                return expression;
            } catch (NoArgumentException e) {
                throw noSecondArgumentError();
            }
        }

        private Expression<T> parseSegments() {
            Expression<T> unary = parseUnary();

            if (unary != null) {
                return unary;
            }

            if (between('0', '9')) {
                return parseConst();
            } else if (test('(')) {
                return parseBrackets();
            } else {
                return parseVariable();
            }
        }

        private Expression<T> parseUnary() {
            if (test('-')) {
                if (between('0', '9')) {
                    return parseConst(true);
                } else {
                    skipWhitespace();

                    return parseUnaryOperation(unaryOperationConstructors.get("-"));
                }
            } else {
                markPosition();

                StringBuilder name = new StringBuilder();

                while (!Character.isWhitespace(ch) && !eof()) {
                    name.append(ch);
                    nextChar();
                }

                final UnaryOperationConstructor<T> operation = unaryOperationConstructors.get(name.toString());

                if (operation == null) {
                    reset();
                    return null;
                } else {
                    return parseUnaryOperation(operation);
                }
            }
        }

        private Expression<T> parseBinaryOperation(Expression<T> expression, BinaryOperationConstructor<T> oneSymbolOperation) {
            return oneSymbolOperation.getConstructor().apply(expression, parseElement(null, oneSymbolOperation.getPriority()), adapter);
        }

        private Expression<T> parseUnaryOperation(UnaryOperationConstructor<T> operation) {
            return operation.getConstructor().apply(parseElement(operation.getPriority()), adapter);
        }


        private Expression<T> parseBrackets() {
            Expression<T> result = parseElement(GlobalPriorities.BASE);

            if (ch == ')') {
                nextChar();
            } else {
                throw noClosingBracketError();
            }

            return result;
        }

        private Expression<T> parseVariable() {
            if (test('x')) {
                return new Variable<>("x");
            } else if (test('y')) {
                return new Variable<>("y");
            } else if (test('z')) {
                return new Variable<>("z");
            } else {
                if (Character.isLetter(ch)) {
                    throw unknownArgumentNameError();
                } else {
                    throw noFirstArgumentError();
                }
            }
        }


        private Expression<T> parseConst() {
            return parseConst(false);
        }

        private Expression<T> parseConst(boolean negative) {
            final StringBuilder sb = new StringBuilder(negative ? "-" : "");
            copyInteger(sb);

            if (test('.')) {
                sb.append('.');
                copyDigits(sb);
            }

            if (test('e') || test('E')) {
                sb.append('e');
                if (test('+')) {
                    // Do nothing
                } else if (test('-')) {
                    sb.append('-');
                }
                copyDigits(sb);
            }

            try {
                return new Const<>(adapter.parseFromString(sb.toString()));
            } catch (NumberFormatException e) {
                throw invalidNumberError();
            }
        }

        private void copyDigits(final StringBuilder sb) {
            while (between('0', '9')) {
                sb.append(ch);
                nextChar();
            }
        }

        private void copyInteger(final StringBuilder sb) {
            if (test('-')) {
                sb.append('-');
            }
            if (test('0')) {
                sb.append('0');
            } else if (between('1', '9')) {
                copyDigits(sb);
            }
        }


        private void skipWhitespace() {
            while (test(' ') || test('\r') || test('\n') || test('\t')) {
                // skip
            }
        }
    }
}
