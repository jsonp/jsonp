package com.alibaba.jsonp.demo;

import javax.json.JsonObject;

import junit.framework.Assert;
import junit.framework.TestCase;

public class Dom_Case0 extends TestCase {

    public void test_toString() throws Exception {
        JsonObject json = new JsonObject();
        json.put("id", 123);
        json.put("name", "jitu");
        
        Assert.assertEquals(2, json.size());
        Assert.assertTrue(json.containsKey("id"));

        Assert.assertEquals("{\"id\":123,\"name\":\"jitu\"}", json.toString());
    }
}
