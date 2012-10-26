/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.json;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Map;

/**
 * A JSON writer.
 * <p>
 * This writer writes a JSON object or array to the stream. For example: <code>
 * <pre>
 * An empty JSON object can be written as follows:
 * 
 * JsonWriter jsonWriter = new JsonWriter(...);
 * jsonWriter.writeObject(new JsonBuilder().beginObject().endObject().build());
 * jsonWriter.close();
 * </pre>
 * </code>
 * 
 * @author Jitendra Kotamraju
 * @author wenshao
 */
public class JsonWriter implements /* Auto */Closeable, Flushable {

    private final Writer writer;

    private char[]       buf;
    private int          pos;

    /**
     * Creates a JSON writer which can be used to write a JSON object or array to the specified i/o writer.
     * 
     * @param writer to which JSON object or array is written
     */
    public JsonWriter(Writer writer){
        this.writer = writer;
        buf = new char[128];
    }

    /**
     * Writes the specified {@link JsonObject}'s representation to the character stream. This method needs to be called
     * only once for a writer instance.
     * 
     * @throws JsonException if the specified JSON object cannot be written due to i/o error
     * @throws IllegalStateException if this method, or writeArray or close method is already called
     */
    public JsonWriter writeObject(JsonObject jsonObject) {
        if (jsonObject == null) {
            return this.writeNull();
        }

        writeBeginObject();

        int index = 0;
        for (Map.Entry<?, ?> entry : jsonObject.entrySet()) {
            if (index != 0) {
                writeObjectPropertySeperator();
            }

            Object key = entry.getKey();
            Object value = entry.getValue();

            writeKeyValue(key, value);
            index++;
        }

        writeEndObject();

        return this;
    }

    /**
     * Writes the specified {@link JsonArray}'s representation to the character stream. This method needs to be called
     * only once for a writer instance.
     * 
     * @throws JsonException if the specified JSON object cannot be written due to i/o error
     * @throws IllegalStateException if this method, or writeObject or close method is already called
     */
    public JsonWriter writeArray(JsonArray jsonArray) {
        if (jsonArray == null) {
            return writeNull();
        }

        writeBeginArray();
        for (int i = 0, size = jsonArray.size(); i < size; ++i) {
            if (i != 0) {
                writeArraySeperator();
            }

            Object item = jsonArray.get(i);
            writeObjectInternal(item);
        }
        writeEndArray();

        return this;
    }

    public JsonWriter writeNull() {
        write("null");
        return this;
    }

    public JsonWriter writeString(String value) {
        if (value == null) {
            return writeNull();
        }

        write('"');
        for (int i = 0; i < value.length(); ++i) {
            char c = value.charAt(i);
            if (c == '"') {
                write("\\\"");
            } else if (c == '\n') {
                write("\\n");
            } else if (c == '\r') {
                write("\\r");
            } else if (c == '\\') {
                write("\\\\");
            } else if (c == '\t') {
                write("\\t");
            } else {
                write(c);
            }
        }
        write('"');

        return this;
    }

    public JsonWriter writeBoolean(boolean value) {
        write(value ? "true" : "false"); // value ? 1 : 0
        return this;
    }

    public JsonWriter writeInt(int i) {
        if (i == Integer.MIN_VALUE) {
            write("-2147483648");
            return this;
        }

        int size = (i < 0) ? stringSize(-i) + 1 : stringSize(i);

        int newcount = pos + size;
        if (newcount > buf.length) {
            flush();
        }

        getChars(i, newcount, buf);

        pos = newcount;

        return this;
    }

    public JsonWriter writeLong(long i) {
        if (i == Long.MIN_VALUE) {
            write("-9223372036854775808");
            return this;
        }

        int size = (i < 0) ? stringSize(-i) + 1 : stringSize(i);

        int newcount = pos + size;
        if (newcount > buf.length) {
            flush();
        }

        getChars(i, newcount, buf);

        pos = newcount;

        return this;
    }

    public JsonWriter writeFloat(float value) {
        if (Float.isNaN(value) || Float.isInfinite(value)) {
            write("null");
        } else {
            write(Float.toString(value));
        }
        return this;
    }

    public JsonWriter writeDouble(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            write("null");
        } else {
            write(Double.toString(value));
        }
        return this;
    }

    public JsonWriter writeBigDecimal(BigDecimal value) {
        write(value.toString());
        return this;
    }

    public JsonWriter writeJavaBean(Object o) {
        throw new JsonException("not support type : " + o.getClass());
    }

    protected JsonWriter writeObjectInternal(Object o) {
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

    public JsonWriter writeKeyValue(Object key, Object value) {
        writeKey(key);
        writeNameValueSeperator();
        writeObjectInternal(value);

        return this;
    }

    public JsonWriter writeKey(Object key) {
        if (key == null) {
            write("\"null\"");
        } else if (key.getClass() == String.class) {
            writeString((String) key);
        } else {
            writeKeyNotString(key);
        }

        return this;
    }

    public JsonWriter writeKeyNotString(Object key) {
        throw new JsonException("not support key type : " + key.getClass());
    }

    public JsonWriter writeBeginArray() {
        write('[');
        return this;
    }

    public JsonWriter writeEndArray() {
        write(']');
        return this;
    }

    public JsonWriter writeArraySeperator() {
        write(',');
        return this;
    }

    public JsonWriter writeBeginObject() {
        write('{');
        return this;
    }

    public JsonWriter writeEndObject() {
        write('}');
        return this;
    }

    public JsonWriter writeObjectPropertySeperator() {
        write(',');
        return this;
    }

    public JsonWriter writeNameValueSeperator() {
        write(':');
        return this;
    }

    protected void write(String text) {
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
                    writer.write(buf, 0, pos);
                    pos = 0;
                    
                    text.getChars(rest, text.length(), buf, 0);
                    pos = text.length() - rest;
                } else {
                    text.getChars(0, text.length(), buf, buf.length);
                }
            }
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    protected void write(char ch) {
        if (pos < buf.length) {
            buf[pos++] = ch;
            return;
        }

        try {
            writer.write(buf);
            pos = 0;
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
    }

    @Override
    public void flush() {
        if (pos == 0) {
            return;
        }

        try {
            writer.write(buf, 0, pos);
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
}
