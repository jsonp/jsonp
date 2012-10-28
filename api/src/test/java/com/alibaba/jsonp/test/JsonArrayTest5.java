package com.alibaba.jsonp.test;

import javax.json.JsonArray;
import javax.json.JsonFactory;
import javax.json.JsonReader;

import junit.framework.Assert;
import junit.framework.TestCase;

public class JsonArrayTest5 extends TestCase {

    public void testArrayEquals() throws Exception {
        JsonArray array = JsonFactory.createJsonArray();

        for (short i = -128; i < 127; ++i) {
            array.add(Integer.toString(i));
        }

        String jsonString = array.toString();

        System.out.println(jsonString);

        JsonReader jsonReader = new JsonReader(jsonString);
        JsonArray array2 = jsonReader.readJsonArray();
        jsonReader.close();

        Assert.assertEquals(array.size(), array2.size());
        for (int i = 0; i < array.size(); ++i) {
            Assert.assertEquals(array.get(i), array2.get(i));
        }
    }
}
