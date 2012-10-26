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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 
 * @author Jitendra Kotamraju
 * @author wenshao
 */
public class JsonArray implements List<Object> {
    private final List<Object> items;

    public JsonArray() {
	items = new ArrayList<Object>();
    }

    public <T> T get(int index, Class<T> clazz) {
	return null;
    }

    @Override
    public int size() {
	return items.size();
    }

    @Override
    public boolean isEmpty() {
	return items.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
	return items.contains(o);
    }

    @Override
    public Iterator<Object> iterator() {
	return items.iterator();
    }

    @Override
    public Object[] toArray() {
	return items.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
	return items.toArray(ts);
    }

    @Override
    public boolean add(Object o) {
	return items.add(o);
    }

    @Override
    public boolean remove(Object o) {
	return items.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> objects) {
	return items.containsAll(objects);
    }

    @Override
    public boolean addAll(Collection<? extends Object> objects) {
	return items.addAll(objects);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Object> objects) {
	return items.addAll(index, objects);
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
	return items.removeAll(objects);
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
	return items.retainAll(objects);
    }

    @Override
    public void clear() {
	items.clear();
    }

    @Override
    public int hashCode() {
	return items.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;

	if (obj == null)
	    return false;

	if (getClass() != obj.getClass())
	    return false;

	return this.items.equals(((JsonArray) obj).items);
    }

    @Override
    public Object get(int index) {
	return items.get(index);
    }

    @Override
    public Object set(int index, Object o) {
	return items.set(index, o);
    }

    @Override
    public void add(int index, Object o) {
	items.add(index, o);
    }

    @Override
    public Object remove(int index) {
	return items.remove(index);
    }

    @Override
    public int indexOf(Object o) {
	return items.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
	return items.lastIndexOf(o);
    }

    @Override
    public ListIterator<Object> listIterator() {
	return items.listIterator();
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
	return items.listIterator(index);
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
	return items.subList(fromIndex, toIndex);
    }

    public String toString() {
	StringBuilder buf = new StringBuilder();

	JsonWriter writer = new JsonWriter(buf);
	writer.writeObjectInternal(this);
	writer.close();

	return buf.toString();
    }
}