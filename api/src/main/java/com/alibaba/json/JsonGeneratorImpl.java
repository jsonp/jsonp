package com.alibaba.json;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonConfiguration;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;

public class JsonGeneratorImpl implements JsonGenerator, Closeable, Flushable {

    private final Writer            writer;

    private char[]                  buf;
    private int                     pos;

    private boolean                 closed = false;

    private final JsonConfiguration config;

    private Context                 context;

    /**
     * Creates a JSON writer which can be used to write a JSON object or array to the specified i/o writer.
     * 
     * @param writer to which JSON object or array is written
     */
    public JsonGeneratorImpl(Writer writer, JsonConfiguration config){
        this.writer = writer;
        buf = new char[128];
        this.config = config;
    }

    public JsonConfiguration getConfig() {
        return config;
    }

    /**
     * Writes the specified {@link JsonObject}'s representation to the character stream. This method needs to be called
     * only once for a writer instance.
     * 
     * @throws JsonException if the specified JSON object cannot be written due to i/o error
     * @throws IllegalStateException if this method, or writeArray or close method is already called
     */
    public JsonGeneratorImpl writeObject(JsonObject jsonObject) {
        if (jsonObject == null) {
            return this.writeNull();
        }

        beginObject();

        for (Map.Entry<?, ?> entry : jsonObject.entrySet()) {
            String key = (String) entry.getKey();
            Object value = entry.getValue();

            writeKeyValue(key, value);
        }

        endObject();

        return this;
    }

    /**
     * Writes the specified {@link JsonArray}'s representation to the character stream. This method needs to be called
     * only once for a writer instance.
     * 
     * @throws JsonException if the specified JSON object cannot be written due to i/o error
     * @throws IllegalStateException if this method, or writeObject or close method is already called
     */
    public JsonGeneratorImpl writeArray(JsonArray jsonArray) {
        if (jsonArray == null) {
            return writeNull();
        }

        beginArray();
        for (int i = 0, size = jsonArray.size(); i < size; ++i) {
            if (i != 0) {
                write(',');
            }

            Object item = jsonArray.get(i);
            writeObjectInternal(item);
        }
        endArray();

        return this;
    }

    public JsonGeneratorImpl writeNull() {
        checkValue();
        write("null");
        return this;
    }

    public JsonGeneratorImpl writeString(String value) {
        return writeString(value, false, true);
    }

    public JsonGeneratorImpl writeString(String value, boolean isKey, boolean checkSpecial) {
        closeCheck();
        if (!isKey) {
            checkValue();
        }

        if (!checkSpecial) {
            write('"');
            write(value);
            write('"');
            return this;
        }

        if (value == null) {
            return writeNull();
        }

        write('"');
        for (int i = 0; i < value.length(); ++i) {
            char c = value.charAt(i);
            if (c == '"') {
                write('\\');
                write('"');
            } else if (c == '\n') {
                write('\\');
                write('n');
            } else if (c == '\r') {
                write('\\');
                write('r');
            } else if (c == '\\') {
                write('\\');
                write('\\');
            } else if (c == '\t') {
                write('\\');
                write('t');
            } else if (c == '\b') {
                write('\\');
                write('b');
            } else if (c == '\f') {
                write('\\');
                write('f');
            } else {
                if (c < ' ' || (c >= '\u0080' && c < '\u00a0') || (c >= '\u2000' && c < '\u2100')) {
                    char u0 = digits[(c >>> 12) & 15];
                    char u1 = digits[(c >>> 8) & 15];
                    char u2 = digits[(c >>> 4) & 15];
                    char u3 = digits[c & 15];

                    write('\\');
                    write('u');
                    write(u0);
                    write(u1);
                    write(u2);
                    write(u3);
                } else {
                    write(c);
                }
            }
        }
        write('"');

        return this;
    }

    public JsonGeneratorImpl writeBoolean(boolean value) {
        checkValue();
        write(value ? "true" : "false"); // value ? 1 : 0
        return this;
    }

    public JsonGeneratorImpl writeInt(int i) {
        closeCheck();
        checkValue();

        if (i == Integer.MIN_VALUE) {
            write("-2147483648");
            return this;
        }

        int size = (i < 0) ? stringSize(-i) + 1 : stringSize(i);

        int newcount = pos + size;
        if (newcount > buf.length) {
            flush();
            getChars(i, size, buf);
            pos = size;
        } else {
            getChars(i, newcount, buf);
            pos = newcount;
        }

        return this;
    }

    public JsonGeneratorImpl writeLong(long i) {
        checkValue();

        if (i == Long.MIN_VALUE) {
            write("-9223372036854775808");
            return this;
        }

        int size = (i < 0) ? stringSize(-i) + 1 : stringSize(i);

        int newcount = pos + size;
        if (newcount > buf.length) {
            flush();
            getChars(i, size, buf);
            pos = size;
        } else {
            getChars(i, newcount, buf);
            pos = newcount;
        }
        return this;
    }

    public JsonGeneratorImpl writeFloat(float value) {
        checkValue();

        if (Float.isNaN(value) || Float.isInfinite(value)) {
            return writeNull();
        } else {
            write(Float.toString(value));
        }
        return this;
    }

    public JsonGeneratorImpl writeDouble(double value) {
        checkValue();

        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return writeNull();
        } else {
            write(Double.toString(value));
        }
        return this;
    }

    public JsonGeneratorImpl writeBigDecimal(BigDecimal value) {
        if (value == null) {
            return writeNull();
        }

        checkValue();

        write(value.toString());
        return this;
    }

    public JsonGeneratorImpl writeDate(Date value) {
        if (value == null) {
            return writeNull();
        }

        checkValue();

        DateFormat dateFormat = config.getDateFormat();
        String formated = dateFormat.format(value);
        writeString(formated);
        return this;
    }

    public JsonGeneratorImpl writeJavaBean(Object o) {
        if (o == null) {
            return writeNull();
        }

        throw new JsonException("not support type : " + o.getClass());
    }

    protected JsonGeneratorImpl writeObjectInternal(Object o) {
        if (o == null) {
            return writeNull();
        }

        Class<?> type = o.getClass();

        if (type == String.class) {
            return writeString((String) o);
        }

        if (type == Boolean.class) {
            return writeBoolean((Boolean) o);
        }

        if (type == Byte.class) {
            return writeInt(((Byte) o).byteValue());
        }

        if (type == Short.class) {
            return writeInt(((Short) o).shortValue());
        }

        if (type == Integer.class) {
            return writeInt(((Integer) o).intValue());
        }

        if (type == Long.class) {
            return writeLong(((Long) o).longValue());
        }

        if (type == Float.class) {
            return writeFloat(((Float) o).floatValue());
        }

        if (type == Double.class) {
            return writeDouble(((Double) o).doubleValue());
        }

        if (type == BigDecimal.class) {
            return writeBigDecimal((BigDecimal) o);
        }

        if (o instanceof JsonObject) {
            return writeObject((JsonObject) o);
        }

        if (o instanceof JsonArray) {
            return writeArray((JsonArray) o);
        }

        return writeJavaBean(o);
    }

    public JsonGeneratorImpl writeKeyValue(String key, Object value) {
        writeKey(key);
        writeObjectInternal(value);

        return this;
    }

    public JsonGeneratorImpl writeKey(String key) {
        if (context == null || context.structureType != JsonStructureType.Object || context.named) {
            throw new JsonException("illegal stat. ");
        }

        if (context.itemsCount > 0) {
            write(',');
        }
        context.named = true;

        if (key == null) {
            write("\"null\"");
        } else if (key.getClass() == String.class) {
            writeString((String) key, true, true);
        } else {
            writeKeyNotString(key);
        }

        return this;
    }

    public JsonGeneratorImpl writeKeyNotString(Object key) {
        throw new JsonException("not support key type : " + key.getClass());
    }

    public JsonGeneratorImpl beginArray() {
        checkValue();
        context = new Context(context, JsonStructureType.Array);
        write('[');
        return this;
    }

    public JsonGeneratorImpl endArray() {
        write(']');
        context = context.parent;
        return this;
    }

    public JsonGeneratorImpl beginObject() {
        checkValue();
        context = new Context(context, JsonStructureType.Object);
        write('{');
        return this;
    }

    public JsonGeneratorImpl endObject() {
        write('}');
        context = context.parent;

        return this;
    }

    private void checkValue() {
        if (context != null) {
            if (context.structureType == JsonStructureType.Object) {
                if (!context.named) {
                    throw new JsonException("require name");
                }
                context.named = false;
                write(':');
            } else {
                if (context.itemsCount > 0) {
                    write(',');
                }
            }
            context.itemsCount++;
        }
    }

    protected void write(String text) {
        closeCheck();

        try {
            if (text.length() > buf.length) {
                if (pos > 0) {
                    flush();
                }
                writer.write(text);
            } else {
                if (text.length() + pos > buf.length) {
                    int rest = buf.length - pos;
                    text.getChars(0, rest, buf, pos);
                    writer.write(buf);
                    pos = 0;

                    text.getChars(rest, text.length(), buf, 0);
                    pos = text.length() - rest;
                } else {
                    text.getChars(0, text.length(), buf, pos);
                    pos += text.length();
                }
            }
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    protected void write(char ch) {
        closeCheck();

        if (pos < buf.length) {
            buf[pos++] = ch;
            return;
        }

        try {
            writer.write(buf);
            buf[0] = ch;
            pos = 1;
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * Closes this JSON writer and frees any resources associated with the writer. This doesn't close the underlying
     * output source.
     */
    @Override
    public void close() {
        flush();

        closed = true;
    }

    void closeCheck() {
        if (closed) {
            throw new IllegalStateException("JsonGenerator is closed.");
        }
    }

    @Override
    public void flush() {
        if (pos == 0) {
            return;
        }

        try {
            writer.write(buf, 0, pos);
            Arrays.fill(buf, '\0');
            pos = 0;
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    static int stringSize(int x) {
        for (int i = 0;; i++)
            if (x <= sizeTable[i]) return i + 1;
    }

    static int stringSize(long x) {
        long p = 10;
        for (int i = 1; i < 19; i++) {
            if (x < p) return i;
            p = 10 * p;
        }
        return 19;
    }

    final static int[]  sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE };

    final static char[] digits    = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    final static char[] DigitTens = { '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1',
            '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3',
            '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5',
            '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7',
            '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', };

    final static char[] DigitOnes = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', };

    static void getChars(int i, int index, char[] buf) {
        int q, r;
        int charPos = index;
        char sign = 0;

        if (i < 0) {
            sign = '-';
            i = -i;
        }

        // Generate two digits per iteration
        while (i >= 65536) {
            q = i / 100;
            // really: r = i - (q * 100);
            r = i - ((q << 6) + (q << 5) + (q << 2));
            i = q;
            buf[--charPos] = DigitOnes[r];
            buf[--charPos] = DigitTens[r];
        }

        // Fall thru to fast mode for smaller numbers
        // assert(i <= 65536, i);
        for (;;) {
            q = (i * 52429) >>> (16 + 3);
            r = i - ((q << 3) + (q << 1)); // r = i-(q*10) ...
            buf[--charPos] = digits[r];
            i = q;
            if (i == 0) break;
        }
        if (sign != 0) {
            buf[--charPos] = sign;
        }
    }

    static void getChars(long i, int index, char[] buf) {
        long q;
        int r;
        int charPos = index;
        char sign = 0;

        if (i < 0) {
            sign = '-';
            i = -i;
        }

        // Get 2 digits/iteration using longs until quotient fits into an int
        while (i > Integer.MAX_VALUE) {
            q = i / 100;
            // really: r = i - (q * 100);
            r = (int) (i - ((q << 6) + (q << 5) + (q << 2)));
            i = q;
            buf[--charPos] = DigitOnes[r];
            buf[--charPos] = DigitTens[r];
        }

        // Get 2 digits/iteration using ints
        int q2;
        int i2 = (int) i;
        while (i2 >= 65536) {
            q2 = i2 / 100;
            // really: r = i2 - (q * 100);
            r = i2 - ((q2 << 6) + (q2 << 5) + (q2 << 2));
            i2 = q2;
            buf[--charPos] = DigitOnes[r];
            buf[--charPos] = DigitTens[r];
        }

        // Fall thru to fast mode for smaller numbers
        // assert(i2 <= 65536, i2);
        for (;;) {
            q2 = (i2 * 52429) >>> (16 + 3);
            r = i2 - ((q2 << 3) + (q2 << 1)); // r = i2-(q2*10) ...
            buf[--charPos] = digits[r];
            i2 = q2;
            if (i2 == 0) break;
        }
        if (sign != 0) {
            buf[--charPos] = sign;
        }
    }

    private static class Context {

        final Context           parent;
        final JsonStructureType structureType;
        int                     itemsCount;
        boolean                 named = false;

        Context(Context parent, JsonStructureType structureType){
            this.parent = parent;
            this.structureType = structureType;
        }
    }
}
