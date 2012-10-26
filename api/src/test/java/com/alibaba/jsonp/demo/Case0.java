package com.alibaba.jsonp.demo;

import javax.json.JsonObject;

public class Case0 {
    public static void main(String[] args) throws Exception {
	JsonObject json = new JsonObject();
	json.put("id", 123);
	json.put("name", "jitu");

	System.out.println(json.toString());
    }
}
