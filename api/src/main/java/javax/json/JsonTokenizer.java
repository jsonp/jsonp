package javax.json;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

public class JsonTokenizer implements Closeable {

    private Reader reader;

    private final char[] buf;
    private int    bufLen;
    private int    index;
    private Token  token;
    private char   ch;

    private String stringValue;
    private long   longValue;
    private double doubleValue;

    /**
     * Creates a JSON reader from a character stream
     * 
     * @param reader a reader from which JSON is to be read
     * @return a JSON reader
     */
    public JsonTokenizer(Reader reader){
        this.reader = reader;
        buf = new char[256];
        this.index = buf.length;
        this.bufLen = buf.length;

        nextChar();
        nextToken();
    }

    public void accept(Token token) {
        if (this.token == token) {
            nextToken();
            return;
        }

        throw new IllegalArgumentException("illegal token : " + this.token + ", expect " + token);
    }

    public Token token() {
        return this.token;
    }

    public long longValue() {
        return this.longValue;
    }

    public double doubleValue() {
        return this.doubleValue;
    }

    public String stringValue() {
        return this.stringValue;
    }

    /**
     * Closes this reader and frees any resources associated with the reader. This doesn't close the underlying input
     * source.
     */
    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new JsonException();
        }
    }

    final void nextToken() {
        if (index == Integer.MIN_VALUE) {
            token = Token.EOF;
            return;
        }

        for (;;) {
            if (ch == ' ' || ch == '\r' || ch == '\n' || ch == '\t') {
                nextChar();
                continue;
            }

            if (index == -1) {
                token = Token.EOF;
                return;
            }

            break;
        }

        switch (ch) {
            case '{':
                token = Token.LBRACE;
                nextChar();
                break;
            case '}':
                token = Token.RBRACE;
                nextChar();
                break;
            case '[':
                token = Token.LBRACKET;
                nextChar();
                break;
            case ']':
                token = Token.RBRACKET;
                nextChar();
                break;
            case ',':
                token = Token.COMMA;
                nextChar();
                break;
            case ':':
                token = Token.COLON;
                nextChar();
                break;
            case '"':
                scanString();
                break;
            case '+':
            case '-':
                scanDigit();
                break;
            default:
                if (isDigit(ch) || ch == '-') {
                    scanDigit();
                    break;
                }

                if (ch == 'n') {
                    nextChar();
                    if (ch == 'u') {
                        nextChar();
                        if (ch == 'l') {
                            nextChar();
                            if (ch == 'l') {
                                nextChar();
                                token = Token.NULL;
                                return;
                            }
                        }
                    }
                }

                if (ch == 't') {
                    nextChar();
                    if (ch == 'r') {
                        nextChar();
                        if (ch == 'u') {
                            nextChar();
                            if (ch == 'e') {
                                nextChar();
                                token = Token.TRUE;
                                return;
                            }
                        }
                    }
                }

                if (ch == 'f') {
                    nextChar();
                    if (ch == 'a') {
                        nextChar();
                        if (ch == 'l') {
                            nextChar();
                            if (ch == 's') {
                                nextChar();
                                if (ch == 'e') {
                                    nextChar();
                                    token = Token.FALSE;
                                    return;
                                }
                            }
                        }
                    }
                }

                throw new JsonException("illegal json char : " + ch);
        }
    }

    static boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private void nextChar() {
        if (index == bufLen) {
            try {
                bufLen = reader.read(buf);
            } catch (IOException e) {
                throw new JsonException(e);
            }

            if (bufLen == -1) {
                index = -1;
                return;
            } else {
                index = 0;
            }
        }

        ch = buf[index++];
    }

    private void scanDigit() {
        boolean isNegate = false;
        if (ch == '-') {
            isNegate = true;
            nextChar();
        }

        int dotCount = 0;
        StringBuilder digitBuf = new StringBuilder();
        for (;;) {
            digitBuf.append(ch);
            nextChar();

            if (ch == '.') {
                dotCount++;
                digitBuf.append('.');
                nextChar();
                continue;
            }

            if (!isDigit(ch)) {
                break;
            }
        }

        if (dotCount == 0) {
            long longValue = Long.parseLong(digitBuf.toString());
            if (isNegate) {
                longValue = -longValue;
            }
            this.longValue = longValue;
            token = Token.INT;
        } else {
            double doubleValue = Double.parseDouble(digitBuf.toString());
            if (isNegate) {
                doubleValue = -doubleValue;
            }
            this.doubleValue = doubleValue;
            token = Token.DOUBLE;
        }
    }

    private void scanString() {
        nextChar();
        StringBuilder strBuf = new StringBuilder();
        for (;;) {
            if (index == -1) {
                throw new IllegalArgumentException("illegal string : " + strBuf);
            }
            if (ch == '"') {
                nextChar();
                break;
            }

            if (ch == '\\') {
                nextChar();
                if (ch == '"' || ch == '\\' || ch == '/') {
                    strBuf.append(ch);
                } else if (ch == 'n') {
                    strBuf.append('\n');
                } else if (ch == 'r') {
                    strBuf.append('\r');
                } else if (ch == 'b') {
                    strBuf.append('\b');
                } else if (ch == 'f') {
                    strBuf.append('\f');
                } else if (ch == 't') {
                    strBuf.append('\t');
                } else {
                    throw new IllegalArgumentException("illegal string : " + strBuf);
                }
            } else {
                strBuf.append(ch);
            }
            nextChar();
        }
        stringValue = strBuf.toString();
        token = Token.STRING;
    }

    public enum Token {
        INT, //
        DOUBLE, //
        STRING, //
        TRUE, //
        FALSE, //
        NULL, //
        EOF, //

        LBRACE("{"), //
        RBRACE("}"), //
        LBRACKET("["), //
        RBRACKET("]"), //
        COMMA(","), //
        COLON(":"),

        ;

        public final String name;

        Token(){
            this(null);
        }

        Token(String name){
            this.name = name;
        }
    }
}
