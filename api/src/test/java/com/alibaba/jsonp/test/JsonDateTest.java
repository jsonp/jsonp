package com.alibaba.jsonp.test;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.json.JsonConfiguration;
import javax.json.spi.JsonProvider;
import javax.json.stream.JsonGenerator;

import junit.framework.Assert;
import junit.framework.TestCase;

public class JsonDateTest extends TestCase {

    public void test_date() throws Exception {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        JsonConfiguration config = new JsonConfiguration();
        config.setDateFormat(dateFormat);
        
        String dateText = "2012-12-01 23:22:21";
        Date date = dateFormat.parse(dateText);
        
        StringWriter writer = new StringWriter();
        
        JsonGenerator jsonWriter = JsonProvider.provider().createGenerator(writer, config);
        
        jsonWriter.writeDate(date);
        jsonWriter.close();
        
        String jsonString = writer.toString();
        
        Assert.assertEquals("\"2012-12-01 23:22:21\"", jsonString);
    }
}
