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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Jitendra Kotamraju
 */
public class JsonArray implements List<Object> {
    public <T>T get(int index, Class<T> clazz) {
        return null;
    }

    public JsonValueType getValueType(int index) {
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
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<Object> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return null;
    }

    @Override
    public boolean add(Object o) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> objects) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Object> objects) {
        return false;
    }

    @Override
    public boolean addAll(int i, Collection<? extends Object> objects) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
        return false;
    }

    @Override
    public void clear() {
    }

    @Override
    public boolean equals(Object o) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int hashCode() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object get(int i) {
        return null;
    }

    @Override
    public Object set(int i, Object o) {
        return null;
    }

    @Override
    public void add(int i, Object o) {
    }

    @Override
    public Object remove(int i) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<Object> listIterator() {
        return null;
    }

    @Override
    public ListIterator<Object> listIterator(int i) {
        return null;
    }

    @Override
    public List<Object> subList(int i, int i1) {
        return null;
    }

    private void test() {
        JsonObject home = new JsonObject();
        home.put("type", "home");
        home.put("number", "212 555-1234");
        JsonObject fax = new JsonObject();
        fax.put("type", "fax");
        fax.put("number", "646 555-4567");

        JsonArray phoneNumber = new JsonArray();
        phoneNumber.add(home);
        phoneNumber.add(fax);

        for(int i=0; i < phoneNumber.size(); i++) {{
            JsonValueType valueType = getValueType(i);
            switch (valueType) {
                case ARRAY:
                    JsonArray array = phoneNumber.get(i, JsonArray.class);
                    break;
                case OBJECT:
                    JsonObject object = phoneNumber.get(i, JsonObject.class);
                    break;
                case STRING:
                    String string = phoneNumber.get(i, String.class);
                    break;
                case NUMBER:
                    BigDecimal number = phoneNumber.get(i, BigDecimal.class);
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

        for(Object value : phoneNumber) {
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
}