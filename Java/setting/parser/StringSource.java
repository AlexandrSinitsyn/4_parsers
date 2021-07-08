package expression.generic.setting.parser;

public class StringSource implements CharSource {

    private final String data;
    private int pos;

    public StringSource(final String data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }


    private int markedPosition = 0;
    @Override
    public void markPosition(int pos) {
        markedPosition = pos;
    }

    @Override
    public int getMarked() {
        return markedPosition;
    }

    @Override
    public void reset() {
        pos = markedPosition - 1;
    }

    @Override
    public ParseException error(final String message) {
        return new ParseException(pos + ": " + message);
    }

    @Override
    public int getPosition() {
        return pos;
    }
}
