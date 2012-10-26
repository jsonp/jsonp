package com.alibaba.jsonp.demo;

import java.io.StringReader;

import javax.json.JsonObject;
import javax.json.JsonReader;

import junit.framework.Assert;
import junit.framework.TestCase;

public class Reader_Case0 extends TestCase {

    public void test_readObject() throws Exception {
        StringReader strReader = new StringReader("{\"id\":123,\"name\":\"jitu\"}");
        JsonReader jsonReader = new JsonReader(strReader);

        Object obj = jsonReader.read();

        JsonObject jsonObject = (JsonObject) obj;

        Assert.assertEquals(2, jsonObject.size());

        Assert.assertEquals(123, jsonObject.get("id"));
        Assert.assertEquals("jitu", jsonObject.get("name"));
    }
}
