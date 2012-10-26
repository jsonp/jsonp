package com.alibaba.jsonp.demo;

import java.io.StringReader;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import junit.framework.Assert;
import junit.framework.TestCase;

public class Reader_Case1 extends TestCase {

    public void test_read_array() throws Exception {
        StringReader strReader = new StringReader("[{\"id\":123,\"name\":\"jitu\"}]");
        JsonReader jsonReader = new JsonReader(strReader);

        Object obj = jsonReader.read();

        JsonArray jsonArray = (JsonArray) obj;

        Assert.assertEquals(1, jsonArray.size());

        JsonObject jsonObject = jsonArray.getJsonObject(0);
        Assert.assertEquals(2, jsonObject.size());

        Assert.assertEquals(123, jsonObject.get("id"));
        Assert.assertEquals("jitu", jsonObject.get("name"));
    }
}
