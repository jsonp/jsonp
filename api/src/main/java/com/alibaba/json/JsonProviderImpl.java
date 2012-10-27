package com.alibaba.json;

import java.io.Reader;
import java.io.Writer;

import javax.json.JsonArray;
import javax.json.JsonConfiguration;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;

public class JsonProviderImpl extends JsonProvider {

    @Override
    public JsonParser createParser(Reader reader) {
	return createParser(reader, new JsonConfiguration());
    }

    @Override
    public JsonParser createParser(Reader reader, JsonConfiguration config) {
	return new JsonParserImpl(reader, config);
    }

    @Override
    public JsonParser createParser(JsonArray array) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public JsonParser createParser(JsonArray array, JsonConfiguration config) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public JsonParser createParser(JsonObject object) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public JsonParser createParser(JsonObject object, JsonConfiguration config) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public JsonGenerator createGenerator(Writer writer) {
	return createGenerator(writer, new JsonConfiguration());
    }

    @Override
    public JsonGenerator createGenerator(Writer writer, JsonConfiguration config) {
	return new JsonGeneratorImpl(writer, config);
    }

}
