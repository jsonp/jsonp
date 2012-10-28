package com.alibaba.json;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonWriter;

public class JsonObjectImpl extends JsonStructureImpl implements JsonObject {

    private final Map<String, Object> map;

    public JsonObjectImpl(){
        map = new LinkedHashMap<String, Object>();
    }

    public JsonObjectImpl(int initialCapacity){
        map = new LinkedHashMap<String, Object>(initialCapacity);
    }

    public JsonObject getJsonObject(String key) {
        return (JsonObject) map.get(key);
    }

    public JsonArray getJsonArray(String key) {
        return (JsonArray) map.get(key);
    }

    public String getString(String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }

        if (value.getClass() == String.class) {
            return (String) value;
        }

        return value.toString();
    }

    public boolean getBooleanValue(String key) {
        return getBooleanValue(key, false);
    }

    public boolean getBooleanValue(String key, boolean defaultValue) {
        Object value = map.get(key);
        return toBooleanValue(value, defaultValue);
    }

    public int getIntValue(String key) {
        return getIntValue(key, 0);
    }

    public int getIntValue(String key, int defaultValue) {
        Object value = map.get(key);
        return toIntValue(value, defaultValue);
    }

    public long getLongValue(String key) {
        return getLongValue(key, 0L);
    }

    public long getLongValue(String key, long defaultValue) {
        Object value = map.get(key);
        return toLongValue(value, defaultValue);
    }

    public BigDecimal getBigDecimal(String key) {
        Object value = map.get(key);
        return toBigDecimal(value);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object name) {
        return map.containsKey(name);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Object get(Object o) {
        return map.get(o);
    }

    @Override
    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    @Override
    public Object remove(Object name) {
        return map.remove(name);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void putAll(Map map) {
        map.putAll(map);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public int hashCode() {
        return map.hashCode();
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

        JsonObjectImpl other = (JsonObjectImpl) obj;
        return this.map.equals(other.map);
    }

    public String toString() {
        try {
            StringWriter buf = new StringWriter();

            JsonWriter writer = new JsonWriter(buf);
            writer.writeObject(this);
            writer.close();

            return buf.toString();
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }
}
