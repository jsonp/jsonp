package com.alibaba.json;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;

import javax.json.JsonException;

public class JsonTokenizer implements Closeable {

    private Reader       reader;

    private final char[] buf;
    private int          bufLen;
    private int          index;
    private JsonToken    token;
    private char         ch;

    private String       stringValue;
    private long         longValue;
    private BigDecimal   doubleValue;
    
    public JsonTokenizer(String text) {
        this (new StringReader(text));
    }

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

    public void accept(JsonToken token) {
        if (this.token == token) {
            nextToken();
            return;
        }

        throw new IllegalArgumentException("illegal token : " + this.token + ", expect " + token);
    }

    public JsonToken token() {
        return this.token;
    }

    public long longValue() {
        return this.longValue;
    }

    public BigDecimal doubleValue() {
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

    public final void nextToken() {
        if (index == Integer.MIN_VALUE) {
            token = JsonToken.EOF;
            return;
        }

        for (;;) {
            if (ch == ' ' || ch == '\r' || ch == '\n' || ch == '\t') {
                nextChar();
                continue;
            }

            if (index == -1) {
                token = JsonToken.EOF;
                return;
            }

            break;
        }

        switch (ch) {
            case '{':
                token = JsonToken.LBRACE;
                nextChar();
                break;
            case '}':
                token = JsonToken.RBRACE;
                nextChar();
                break;
            case '[':
                token = JsonToken.LBRACKET;
                nextChar();
                break;
            case ']':
                token = JsonToken.RBRACKET;
                nextChar();
                break;
            case ',':
                token = JsonToken.COMMA;
                nextChar();
                break;
            case ':':
                token = JsonToken.COLON;
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
                                token = JsonToken.NULL;
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
                                token = JsonToken.TRUE;
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
                                    token = JsonToken.FALSE;
                                    return;
                                }
                            }
                        }
                    }
                }

                throw new JsonException("illegal json char : " + ch);
        }
    }

    private int             matchState;

    public final static int NOT_MATCH      = -1;
    public final static int NOT_MATCH_NAME = -2;
    public final static int UNKOWN         = 0;
    public final static int OBJECT         = 1;
    public final static int ARRAY          = 2;
    public final static int VALUE          = 3;
    public final static int END            = 4;

    public int matchFieldInt(String fieldName) {
        matchState = UNKOWN;
        
        if (ch != '"') {
            matchState = NOT_MATCH_NAME;
            return 0;
        }
        nextChar();
        
        for (int i = 0; i < fieldName.length(); ++i) {
            if (fieldName.charAt(i) != ch) {
                matchState = NOT_MATCH_NAME;
                return 0;
            }
            nextChar();
        }
        
        if (ch != '"') {
            matchState = NOT_MATCH_NAME;
            return 0;
        }
        nextChar();
        
        if (ch != ':') {
            matchState = NOT_MATCH_NAME;
            return 0;
        }
        nextChar();
        
        boolean nagative = false;
        if (ch == '-') {
            nagative = true;
            nextChar();
        }
        
        int value = 0;
        if (ch >= '0' && ch <= '9') {
            value = ch - '0';
            nextChar();
            for (;;) {
                if (ch >= '0' && ch <= '9') {
                    value = value * 10 + ((int) ch - '0');
                } else if (ch == '.') {
                    matchState = NOT_MATCH;
                    return 0;
                } else {
                    break;
                }
                nextChar();
            }
            if (value < 0) { // overflow
                matchState = NOT_MATCH;
                return 0;
            }
        } else {
            matchState = NOT_MATCH;
            return 0;
        }
        if (nagative) {
            value = -value;
        }
        
        if (ch == ',') {
            nextChar();
            matchState = VALUE;
            token = JsonToken.COMMA;
            return value;
        }

        if (ch == '}') {
            nextChar();
            if (ch == ',') {
                token = JsonToken.COMMA;
                nextChar();
            } else if (ch == ']') {
                token = JsonToken.RBRACKET;
                nextChar();
            } else if (ch == '}') {
                token = JsonToken.RBRACE;
                nextChar();
            } else {
                matchState = NOT_MATCH;
                return 0;
            }
            matchState = END;
        }

        return value;
    }
    
    public int getMatchState() {
        return matchState;
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
        int dotCount = 0;
        StringBuilder digitBuf = new StringBuilder();

        if (ch == '-' || ch == '+') {
            digitBuf.append(ch);
            nextChar();
        }

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

        if (ch == 'E' || ch == 'e') {
            digitBuf.append(ch);
            nextChar();
            if (ch == '+' || ch == '-') {
                digitBuf.append(ch);
                nextChar();
            }

            while (isDigit(ch)) {
                digitBuf.append(ch);
                nextChar();
            }
        }

        if (dotCount == 0) {
            this.longValue = Long.parseLong(digitBuf.toString());
            token = JsonToken.INT;
        } else {
            this.doubleValue = new BigDecimal(digitBuf.toString());
            token = JsonToken.DOUBLE;
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
                } else if (ch == 'u') {
                    char u1, u2, u3, u4;

                    nextChar();
                    u1 = ch;
                    nextChar();
                    u2 = ch;
                    nextChar();
                    u3 = ch;
                    nextChar();
                    u4 = ch;

                    int charCode = hex(u1) * 4096 + hex(u2) * 256 + hex(u3) * 16 + hex(u4);

                    strBuf.append((char) charCode);
                } else {
                    throw new IllegalArgumentException("illegal string : " + strBuf);
                }
            } else {
                strBuf.append(ch);
            }
            nextChar();
        }
        stringValue = strBuf.toString();
        token = JsonToken.STRING;
    }

    static int hex(char ch) {
        if (ch >= '0' && ch <= '9') {
            return ch - '0';
        }

        if (ch >= 'A' && ch <= 'F') {
            return ch - 'A' + 10;
        }

        if (ch >= 'a' && ch <= 'f') {
            return ch - 'a' + 10;
        }

        throw new IllegalArgumentException("illegal hex : " + ch);
    }
}
