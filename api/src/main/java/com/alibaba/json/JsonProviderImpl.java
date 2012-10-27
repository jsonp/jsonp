package com.alibaba.json;

import java.io.Reader;
import java.io.Writer;

import javax.json.JsonConfiguration;
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
    public JsonGenerator createGenerator(Writer writer) {
	return createGenerator(writer, new JsonConfiguration());
    }

    @Override
    public JsonGenerator createGenerator(Writer writer, JsonConfiguration config) {
	return new JsonGeneratorImpl(writer, config);
    }

}
