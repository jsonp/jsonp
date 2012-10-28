package com.alibaba.jsonp.test;

import javax.json.JsonArray;
import javax.json.JsonFactory;
import javax.json.JsonReader;

import junit.framework.Assert;
import junit.framework.TestCase;

public class JsonArrayTest extends TestCase {
    public void testArrayEquals() throws Exception {
        JsonArray array = JsonFactory.createJsonArray();
        
        array.add(true);
        array.add(false);
        array.add(null);
        for (int i = -128; i <= 127; ++i) {
            array.add(i);
        }
        array.add(Integer.MIN_VALUE);
        array.add(Integer.MAX_VALUE);
        array.add(Long.MIN_VALUE);
        array.add(Long.MAX_VALUE);

        String jsonString = array.toString();
        
        JsonReader jsonReader = new JsonReader(jsonString);
        JsonArray array2 = jsonReader.readJsonArray();
        jsonReader.close();
        
        Assert.assertEquals(array.size(), array2.size());
        for (int i = 0; i < array.size(); ++i) {
            Assert.assertEquals(array.get(i), array2.get(i));
        }
    }
}
