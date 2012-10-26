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

package javax.json;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jitendra Kotamraju
 */
public class JsonObject implements Map<String, Object> {
    public <T>T get(String key, Class<T> clazz) {
        return null;
    }

    public JsonValueType getValueType(String key) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object o) {
        return false;
    }

    @Override
    public boolean containsValue(Object o) {
        return false;
    }

    @Override
    public Object get(Object o) {
        return null;
    }

    @Override
    public Object put(String s, Object o) {
        return null;
    }

    @Override
    public Object remove(Object o) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> map) {
    }

    @Override
    public void clear() {
    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<Object> values() {
        return null;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return null;
    }

    private void test() {
        JsonObject address = new JsonObject();
        address.put("streetAddress", "21 2nd Street");
        address.put("city", "New York");
        address.put("state", "NY");
        address.put("postalCode", "10021");

        JsonObject home = new JsonObject();
        home.put("type", "home");
        home.put("number", "212 555-1234");
        JsonObject fax = new JsonObject();
        fax.put("type", "fax");
        fax.put("number", "646 555-4567");

        JsonArray phoneNumber = new JsonArray();
        phoneNumber.add(home);
        phoneNumber.add(fax);

        JsonObject person = new JsonObject();
        person.put("firstName", "John");
        person.put("lastName", "Smith");
        person.put("age", 25);
        person.put("address", address);
        person.put("phoneNumber", phoneNumber);

        String firstName = person.get("firstName", String.class);

        for(String key : person.keySet()) {
            JsonValueType valueType = getValueType(key);
            switch (valueType) {
                case ARRAY:
                    JsonArray array = person.get(key, JsonArray.class);
                    break;
                case OBJECT:
                    JsonObject object = person.get(key, JsonObject.class);
                    break;
                case STRING:
                    String string = person.get(key, String.class);
                    break;
                case NUMBER:
                    BigDecimal number = person.get(key, BigDecimal.class);
                    break;
                case TRUE:
                    // value would be true
                    break;
                case FALSE:
                    // value would be false
                    break;
                case NULL:
                    // value would be null
                    break;
            }
        }

        for(Entry<String, Object> entry : person.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                // JSON null
            } else if (value instanceof Boolean) {
                // JSON true or false
            } else if (value instanceof String) {
                String string = (String)value;
            } else if (value instanceof BigDecimal) {
                BigDecimal number = (BigDecimal)value;
            } else if (value instanceof JsonArray) {
                JsonArray array = (JsonArray)value;
            } else if (value instanceof JsonObject) {
                JsonObject object = (JsonObject)value;
            } else {
                throw new IllegalArgumentException();
            }
        }

    }
}