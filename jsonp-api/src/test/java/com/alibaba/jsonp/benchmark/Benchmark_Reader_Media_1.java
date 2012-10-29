package com.alibaba.jsonp.benchmark;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.json.JsonObject;
import javax.json.JsonReader;

import junit.framework.TestCase;

public class Benchmark_Reader_Media_1 extends TestCase {
    private String text;

    public void test_readMedia_1() throws Exception {

        StringWriter writer = new StringWriter();
        
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("media.1.json");
        Reader reader = new InputStreamReader(in);
        
        char[] buf = new char[1024];
        for (;;) {
            int len = reader.read(buf);
            if (len == -1) {
                break;
            }
            
            if (len > 0) {
                writer.write(buf, 0, len);
            }
        }
        reader.close();
        
        text = writer.toString();
        
        for (int i = 0; i < 10; ++i) {
            perf();
        }
    }

    private void perf() throws IOException {
        long startMillis = System.currentTimeMillis();
        for (int i = 0; i < 1000; ++i) {
            Reader reader = new StringReader(text);

            JsonReader jsonReader = new JsonReader(reader);

            JsonObject jsonObj = jsonReader.readJsonObject();

            jsonReader.close();
        }
        long millis = System.currentTimeMillis() - startMillis;
        System.out.println("millis : " + millis);
    }

}
