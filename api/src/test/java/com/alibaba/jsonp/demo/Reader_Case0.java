package com.alibaba.jsonp.demo;

import java.io.StringReader;

import javax.json.JsonObject;
import javax.json.JsonReader;

public class Reader_Case0 {
    public static void main(String[] args) throws Exception {
	StringReader strReader = new StringReader("{\"id\":123,\"name\":\"jitu\"}");
	JsonReader jsonReader = new JsonReader(strReader);
	
	Object obj = jsonReader.read();
	
	JsonObject jsonObj = (JsonObject) obj;
	
	System.out.println(jsonObj.toString());
    }
}
