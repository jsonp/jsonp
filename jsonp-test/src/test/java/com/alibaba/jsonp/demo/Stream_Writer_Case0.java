package com.alibaba.jsonp.demo;

import java.io.StringWriter;

import javax.json.spi.JsonProvider;
import javax.json.stream.JsonGenerator;

import junit.framework.Assert;
import junit.framework.TestCase;

public class Stream_Writer_Case0 extends TestCase {

    public void test_stream_write() throws Exception {
        StringWriter buf = new StringWriter();

        JsonGenerator writer = JsonProvider.provider().createGenerator(buf);

        writer //
        .beginObject() //
        .writeKeyValue("id", 123) //
        .writeKeyValue("name", "jitu") //
        .endObject() //
        .close();
        
        Assert.assertEquals("{\"id\":123,\"name\":\"jitu\"}", buf.toString());
    }
}
