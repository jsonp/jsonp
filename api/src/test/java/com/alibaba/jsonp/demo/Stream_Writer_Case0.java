package com.alibaba.jsonp.demo;

import java.io.StringWriter;

import javax.json.JsonWriter;

import junit.framework.Assert;
import junit.framework.TestCase;

public class Stream_Writer_Case0 extends TestCase {

    public void test_stream_write() throws Exception {
        StringWriter buf = new StringWriter();

        JsonWriter writer = new JsonWriter(buf);

        writer //
        .writeBeginObject() //
        .writeKeyValue("id", 123) //
        .writeObjectPropertySeperator() //
        .writeKeyValue("name", "jitu") //
        .writeEndObject() //
        .close();
        
        Assert.assertEquals("{\"id\":123,\"name\":\"jitu\"}", buf.toString());
    }
}
