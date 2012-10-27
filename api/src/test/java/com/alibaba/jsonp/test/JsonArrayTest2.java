package com.alibaba.jsonp.test;

import javax.json.JsonArray;
import javax.json.JsonReader;

import junit.framework.Assert;
import junit.framework.TestCase;

public class JsonArrayTest2 extends TestCase {
    public void testArrayEquals() throws Exception {
        JsonArray array = new JsonArray();
        
        array.add(true);
        array.add(false);
        array.add(null);
        array.add(0);
        array.add(Integer.MIN_VALUE);
        array.add(Integer.MAX_VALUE);
        array.add(Long.MIN_VALUE);
        array.add(Long.MAX_VALUE);
        array.add(0.0);
        array.add(Float.MIN_VALUE);
        array.add(Float.MAX_VALUE);
        array.add(Double.MIN_VALUE);
        array.add(Double.MAX_VALUE);

        String jsonString = array.toString();
        
        System.out.println(jsonString);
        
        JsonReader jsonReader = new JsonReader(jsonString);
        JsonArray array2 = jsonReader.readJsonArray();
        jsonReader.close();
        
        Assert.assertEquals(array, array2);
    }
}
