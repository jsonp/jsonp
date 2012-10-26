package com.alibaba.jsonp.demo;

import java.io.StringReader;
import java.util.Iterator;

import javax.json.JsonStreamReader;
import javax.json.JsonStreamReader.Context;
import javax.json.JsonStreamReader.Event;

import junit.framework.TestCase;

public class StreamReader_Case0 extends TestCase {
    public void test_streamReader_0() {
	StringReader strReader = new StringReader(
		"[{\"id\":123,\"name\":\"jitu\"},{\"id\":234,\"name\":\"wenshao\"}]");
	JsonStreamReader jsonStreamReader = new JsonStreamReader(strReader);

	Iterator<Event> events = jsonStreamReader.iterator();
	for (; events.hasNext();) {
	    Context context = jsonStreamReader.getContext();

	    Event event = events.next();

	    if (context != null) {
		for (int i = 0; i <= context.getLevel(); ++i) {
		    System.out.print("\t");
		}
	    }

	    System.out.print(event);
	    System.out.println();
	}
    }
}
