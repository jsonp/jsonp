package com.alibaba.jsonp.demo;

import java.io.StringReader;
import java.util.Iterator;

import javax.json.spi.JsonProvider;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import junit.framework.TestCase;

public class StreamReader_Case0 extends TestCase {

    public void test_streamReader_0() {
	String text = "[{\"id\":123,\"name\":\"jitu\"},{\"name\":\"wenshao\", \"id\":234},{\"x\":[{},true,false,null,1.0,1,\"xx\",[[[]]]]},{}]";
	StringReader strReader = new StringReader(text);
	JsonParser parser = JsonProvider.provider().createParser(strReader);

	Iterator<Event> events = parser.iterator();
	Event lastEvent = null;
	for (; events.hasNext();) {
	    Event event = events.next();

	    if (lastEvent != Event.KEY_NAME) {
		System.out.println();
		
		for (int i = 0; i < parser.getDepth(); ++i) {
		    System.out.print("\t");
		}
	    }

	    switch (event) {
	    case START_OBJECT:
		System.out.print('{');
		break;
	    case END_OBJECT:
		System.out.print('}');
		break;
	    case START_ARRAY:
		System.out.print('[');
		break;
	    case END_ARRAY:
		System.out.print(']');
		break;
	    case KEY_NAME:
		System.out.print("\t\"");
		System.out.print(parser.getString());
		System.out.print("\" : ");
		break;
	    case VALUE_FALSE:
		System.out.print("false");
		break;
	    case VALUE_TRUE:
		System.out.print("true");
		break;
	    case VALUE_NULL:
		System.out.print("null");
		break;
	    case VALUE_STRING:
		System.out.print(parser.getString());
		break;
	    case VALUE_NUMBER:
		System.out.print(parser.getBigDecimalValue());
		break;
	    default:
		break;
	    }

	    lastEvent = event;
	}
    }
}
