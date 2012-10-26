package com.alibaba.jsonp.demo;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import junit.framework.Assert;
import junit.framework.TestCase;

public class Reader_Case2 extends TestCase {

    public void test_readMedia_1() throws Exception {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("media.1.json");
        Reader reader = new InputStreamReader(in);

        JsonReader jsonReader = new JsonReader(reader);

        JsonObject jsonObj = jsonReader.readJsonObject();
        Assert.assertEquals(2, jsonObj.size());
        
        JsonObject media = jsonObj.getJsonObject("Media");
        
        Assert.assertNotNull(media);
        Assert.assertEquals(11, media.size());

        Assert.assertEquals("http://javaone.com/keynote.mpg", media.get("Uri"));
        Assert.assertEquals("http://javaone.com/keynote.mpg", media.getString("Uri"));
        
        JsonArray images = jsonObj.getJsonArray("Images");
        
        Assert.assertNotNull(images);
        
        jsonReader.close();
    }
}
