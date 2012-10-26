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
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * A JSON writer.
 * 
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
 */
public class JsonWriter implements /* Auto */Closeable {
    private final Appendable writer;

    /**
     * Creates a JSON writer which can be used to write a JSON object or array
     * to the specified i/o writer.
     * 
     * @param writer
     *            to which JSON object or array is written
     */
    public JsonWriter(Appendable writer) {
	this.writer = writer;
    }

    /**
     * Writes the specified {@link JsonObject}'s representation to the character
     * stream. This method needs to be called only once for a writer instance.
     * 
     * @throws JsonException
     *             if the specified JSON object cannot be written due to i/o
     *             error
     * @throws IllegalStateException
     *             if this method, or writeArray or close method is already
     *             called
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
     * Writes the specified {@link JsonArray}'s representation to the character
     * stream. This method needs to be called only once for a writer instance.
     * 
     * @throws JsonException
     *             if the specified JSON object cannot be written due to i/o
     *             error
     * @throws IllegalStateException
     *             if this method, or writeObject or close method is already
     *             called
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
	appendString("null");
	return this;
    }

    public JsonWriter writeString(String value) {
	if (value == null) {
	    return writeNull();
	}

	appendChar('"');
	for (int i = 0; i < value.length(); ++i) {
	    char c = value.charAt(i);
	    if (c == '"') {
		appendString("\\\"");
	    } else if (c == '\n') {
		appendString("\\n");
	    } else if (c == '\r') {
		appendString("\\r");
	    } else if (c == '\\') {
		appendString("\\\\");
	    } else if (c == '\t') {
		appendString("\\t");
	    } else {
		appendChar(c);
	    }
	}
	appendChar('"');

	return this;
    }

    public JsonWriter writeBoolean(boolean value) {
	appendString(value ? "true" : "false"); // value ? 1 : 0
	return this;
    }

    public JsonWriter writeInt(int value) {
	appendString(Integer.toString(value));
	return this;
    }

    public JsonWriter writeLong(long value) {
	appendString(Long.toString(value));
	return this;
    }

    public JsonWriter writeFloat(float value) {
	if (Float.isNaN(value) || Float.isInfinite(value)) {
	    appendString("null");
	} else {
	    appendString(Float.toString(value));
	}
	return this;
    }

    public JsonWriter writeDouble(double value) {
	if (Double.isNaN(value) || Double.isInfinite(value)) {
	    appendString("null");
	} else {
	    appendString(Double.toString(value));
	}
	return this;
    }

    public JsonWriter writeBigDecimal(BigDecimal value) {
	appendString(value.toString());
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
	    appendString("\"null\"");
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
	appendChar('[');
	return this;
    }

    public JsonWriter writeEndArray() {
	appendChar(']');
	return this;
    }

    public JsonWriter writeArraySeperator() {
	appendChar(',');
	return this;
    }

    public JsonWriter writeBeginObject() {
	appendChar('{');
	return this;
    }

    public JsonWriter writeEndObject() {
	appendChar('}');
	return this;
    }

    public JsonWriter writeObjectPropertySeperator() {
	appendChar(',');
	return this;
    }

    public JsonWriter writeNameValueSeperator() {
	appendChar(':');
	return this;
    }

    protected void appendString(String text) {
	try {
	    writer.append(text);
	} catch (IOException e) {
	    throw new JsonException(e);
	}
    }

    protected void appendChar(char ch) {
	try {
	    writer.append(ch);
	} catch (IOException e) {
	    throw new JsonException(e);
	}
    }

    /**
     * Closes this JSON writer and frees any resources associated with the
     * writer. This doesn't close the underlying output source.
     */
    @Override
    public void close() {

    }

}