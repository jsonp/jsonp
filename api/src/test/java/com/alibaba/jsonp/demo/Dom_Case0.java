package com.alibaba.jsonp.demo;

import javax.json.JsonArray;
import javax.json.JsonFactory;
import javax.json.JsonObject;

import junit.framework.Assert;
import junit.framework.TestCase;

public class Dom_Case0 extends TestCase {

    public void test_toString() throws Exception {
        JsonObject json = JsonFactory.createJsonObject();
        json.put("id", 123);
        json.put("name", "jitu");
        
        Assert.assertEquals(2, json.size());
        Assert.assertTrue(json.containsKey("id"));

        Assert.assertEquals("{\"id\":123,\"name\":\"jitu\"}", json.toString());
    }
    
    public void test_array_toString() throws Exception {
        JsonArray jsonArray = JsonFactory.createJsonArray();
        jsonArray.add(123);
        jsonArray.add("jitu");
        jsonArray.add(JsonFactory.createJsonObject());
        
        Assert.assertEquals("[123,\"jitu\",{}]", jsonArray.toString());
    }
}
