package com.alibaba.jsonp.demo;

import java.io.StringReader;
import java.util.Iterator;

import javax.json.JsonStreamReader;
import javax.json.JsonStreamReader.Context;
import javax.json.JsonStreamReader.Event;

import junit.framework.TestCase;

public class StreamReader_Case0 extends TestCase {

    public void test_streamReader_0() {
        String text = "[{\"id\":123,\"name\":\"jitu\"},{\"name\":\"wenshao\", \"id\":234},[{},true,false,null,1.0,1,\"xx\",[[[]]]],{}]";
        StringReader strReader = new StringReader(text);
        JsonStreamReader jsonStreamReader = new JsonStreamReader(strReader);

        Iterator<Event> events = jsonStreamReader.iterator();
        for (; events.hasNext();) {
            Event event = events.next();
            Context context = jsonStreamReader.getContext();

            if (context != null) {
                for (int i = 0; i < context.getLevel(); ++i) {
                    System.out.print("\t");
                }
                if (event != Event.START_OBJECT //
                    && event != Event.END_OBJECT //
                    && event != Event.START_ARRAY //
                    && event != Event.END_ARRAY //
                    ) {
                    System.out.print("\t");
                }
            }

            System.out.print(event);
            System.out.println();
        }
    }
}
