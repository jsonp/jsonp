/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2012 Oracle and/or its affiliates. All rights reserved.
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

package javax.json.stream;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValueType;
import java.io.Closeable;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Map;

public class JsonParser implements /*Auto*/Closeable {

    /**
     * Creates a JSON parser from a character stream
     *
     * @param reader a i/o reader from which JSON is to be read
     */
    public JsonParser(Reader reader) {
    }

    /**
     * Creates a JSON parser from a JSON array
     *
     * @param array a JSON array
     */
    public JsonParser(JsonArray array) {
    }

    /**
     * Creates a JSON parser from a JSON object
     *
     * @param object a JSON object
     */
    public JsonParser(JsonObject object) {
    }

    public JsonValueType getValueType() {
        return null;
    }

    /**
     * @return JsonObjectIterator, JsonArrayIterator, String, BigDecimal,
     * Boolean.TRUE, Boolean.FALSE, null
     */
    public Object getValue() {
        return null;
    }

    /**
     * Closes this parser and frees any resources associated with the
     * parser. This doesn't close the underlying input source.
     */
    @Override
    public void close() {
    }

    private void testEmptyArray() throws Exception {
        Reader reader = new StringReader("{}");
        JsonParser parser = new JsonParser(reader);
        JsonValueType valueType = parser.getValueType();
        assert valueType == JsonValueType.ARRAY;
        JsonArrayIterator it = (JsonArrayIterator)parser.getValue();
        assert !it.hasNext();

        parser.close();
        reader.close();
    }

    private void testEmptyObject() throws Exception {
        Reader reader = new StringReader("{}");
        JsonParser parser = new JsonParser(reader);
        JsonValueType valueType = parser.getValueType();
        assert valueType == JsonValueType.OBJECT;
        JsonObjectIterator it = (JsonObjectIterator)parser.getValue();
        assert !it.hasNext();

        parser.close();
        reader.close();
    }

    private void testParser() throws Exception {
        Reader reader = null;
        JsonParser parser = new JsonParser(reader);
        parse(parser.getValueType(), parser.getValue());
        parser.close();
        reader.close();
    }

    private void parse(JsonValueType valueType, Object value) {
        switch (valueType) {
            case ARRAY:
                JsonArrayIterator arrayIt = (JsonArrayIterator)value;
                while(arrayIt.hasNext()) {
                    parse(arrayIt.next(), arrayIt.getValue());
                }
                break;
            case OBJECT:
                JsonObjectIterator objectIt = (JsonObjectIterator)value;
                while(objectIt.hasNext()) {
                    Map.Entry<String, JsonValueType> entry = objectIt.next();
                    entry.getKey();
                    parse(entry.getValue(), objectIt.getValue());
                }
                break;
            case STRING:
                String string = (String)value;
                break;
            case NUMBER:
                BigDecimal number = (BigDecimal)value;
                break;
            case TRUE:
                assert (Boolean)value;
                break;
            case FALSE:
                assert !(Boolean)value;
                break;
            case NULL:
                assert value == null;
                break;
        }
    }

}
