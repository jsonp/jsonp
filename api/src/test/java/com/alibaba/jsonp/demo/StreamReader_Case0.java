package com.alibaba.jsonp.demo;

import java.io.StringReader;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class StreamReader_Case0 {
    public static void main(String[] args) throws Exception {
	StringReader strReader = new StringReader("[{\"id\":123,\"name\":\"jitu\"},{\"id\":234,\"name\":\"wenshao\"}]");
	JsonReader jsonReader = new JsonReader(strReader);
	
	Object obj = jsonReader.read();
	
	JsonArray jsonArray = (JsonArray) obj;
	
	System.out.println(jsonArray.toString());
    }
}
