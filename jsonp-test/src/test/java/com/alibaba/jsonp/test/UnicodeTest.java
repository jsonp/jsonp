package com.alibaba.jsonp.test;

import java.io.StringReader;

import javax.json.JsonObject;
import javax.json.JsonReader;

import junit.framework.Assert;
import junit.framework.TestCase;

public class UnicodeTest extends TestCase {

    public void test_readObject() throws Exception {

        String text = "{\"id\":123,\"name\":\"jitu\",\"country\":\"\\u4E2D\\u56FD\"}";
        StringReader strReader = new StringReader(text);
        JsonReader jsonReader = new JsonReader(strReader);

        Object obj = jsonReader.read();

        JsonObject jsonObject = (JsonObject) obj;

        Assert.assertEquals(3, jsonObject.size());

        Assert.assertEquals(123, jsonObject.get("id"));
        Assert.assertEquals("jitu", jsonObject.get("name"));
        Assert.assertEquals("中国", jsonObject.get("country"));
        
        String text2 = jsonObject.toString();
        System.out.println(text2);
    }
}
