package com.alibaba.jsonp.demo;

import javax.json.JsonWriter;

public class Case1 {
    public static void main(String[] args) throws Exception {
	StringBuilder buf = new StringBuilder();

	JsonWriter writer = new JsonWriter(buf);

	writer.writeBeginObject() //
		.writeKeyValue("id", 123) //
		.writeObjectPropertySeperator() //
		.writeKeyValue("name", "jitu") //
		.writeEndObject() //
		.close();

	System.out.println(buf.toString());
    }
}
