package expression.generic.setting.parser;

public interface CharSource {

    int getPosition();

    boolean hasNext();

    char next();

    void markPosition(int pos);

    int getMarked();

    void reset();

    ParseException error(final String message);
}
