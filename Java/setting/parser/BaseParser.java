package expression.generic.setting.parser;

import expression.generic.setting.exceptions.types.*;

public class BaseParser {
    public static final char END = '\0';
    private final CharSource source;
    protected char ch = 0xffff;

    protected BaseParser(final CharSource source) {
        super();
        this.source = source;
    }

    protected void nextChar() {
        ch = source.hasNext() ? source.next() : END;
    }

    protected void markPosition() {
        if (eof()) {
            source.markPosition(-1);
        } else {
            source.markPosition(source.getPosition());
        }
    }

    protected void reset() {
        if (source.getMarked() == -1) {
            // do nothing
        } else {
            source.reset();
            nextChar();
        }
    }

    protected boolean test(char expected) {
        return test(expected, true);
    }

    protected boolean test(char expected, boolean skip) {
        if (ch == expected) {
            if (skip) {
                nextChar();
            }
            return true;
        }
        return false;
    }

    protected void expect(final char c) {
        if (ch != c) {
            throw error("Expected '" + c + "', found '" + ch + "'");
        }
        nextChar();
    }

    protected void expect(final String value) {
        for (char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }


    protected boolean eof() {
        return test(END);
    }


    protected ParseException error(final String message) {
        return source.error(message);
    }

    protected EndOfExpressionException expectedEndOfExpressionError() {
        return new EndOfExpressionException(source.getPosition());
    }

    protected InvalidNumberException invalidNumberError() {
        return new InvalidNumberException(source.getPosition());
    }

    protected NoOperationException noOperationError() {
        return new NoOperationException(source.getPosition());
    }

    protected NoSeparatorException noSeparatorError() {
        return new NoSeparatorException(source.getPosition());
    }

    protected NoBracketException noOpeningBracketError() {
        return new NoBracketException(source.getPosition(), true);
    }

    protected NoBracketException noClosingBracketError() {
        return new NoBracketException(source.getPosition(), false);
    }

    protected NoArgumentException noFirstArgumentError() {
        return new NoArgumentException(source.getPosition(), true);
    }

    protected NoArgumentException noSecondArgumentError() {
        return new NoArgumentException(source.getPosition(), false);
    }

    protected UnknownArgumentNameException unknownArgumentNameError() {
        return new UnknownArgumentNameException(ch, source.getPosition());
    }
}
