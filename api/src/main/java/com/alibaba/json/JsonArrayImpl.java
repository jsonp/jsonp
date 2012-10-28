package com.alibaba.json;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonWriter;


public class JsonArrayImpl extends JsonStructureImpl implements JsonArray {
    private final List<Object> items;

    public JsonArrayImpl() {
        items = new ArrayList<Object>();
    }

    public JsonObject getJsonObject(int index) {
        return (JsonObject) get(index);
    }

    public JsonArray getJsonArray(int index) {
        return (JsonArray) get(index);
    }
    
    public String getString(int index) {
        Object value = items.get(index);
        if (value == null) {
            return null;
        }

        if (value.getClass() == String.class) {
            return (String) value;
        }

        return value.toString();
    }

    public boolean getBooleanValue(int index) {
        return getBooleanValue(index, false);
    }

    public boolean getBooleanValue(int index, boolean defaultValue) {
        Object value = items.get(index);
        return toBooleanValue(value, defaultValue);
    }

    public int getIntValue(int index) {
        return getIntValue(index, 0);
    }

    public int getIntValue(int index, int defaultValue) {
        Object value = items.get(index);
        return toIntValue(value, defaultValue);
    }

    public long getLongValue(int index) {
        return getLongValue(index, 0L);
    }

    public long getLongValue(int index, long defaultValue) {
        Object value = items.get(index);
        return toLongValue(value, defaultValue);
    }

    public BigDecimal getBigDecimal(int index) {
        Object value = items.get(index);
        return toBigDecimal(value);
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
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        return this.items.equals(((JsonArrayImpl) obj).items);
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
        try {
            StringWriter buf = new StringWriter();

            JsonWriter writer = new JsonWriter(buf);
            writer.writeArray(this);
            writer.close();

            return buf.toString();
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

}
