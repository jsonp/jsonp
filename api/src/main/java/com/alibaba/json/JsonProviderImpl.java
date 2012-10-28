package com.alibaba.json;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

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
    public JsonGenerator createGenerator(Writer writer) {
	return createGenerator(writer, new JsonConfiguration());
    }

    @Override
    public JsonGenerator createGenerator(Writer writer, JsonConfiguration config) {
	return new JsonGeneratorImpl(writer, config);
    }

    @Override
    public JsonObject createJsonObject() {
        return new JsonObjectImpl();
    }

    @Override
    public JsonArray createJsonArray() {
        return new JsonArrayImpl();
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out) {
        return createGenerator(new OutputStreamWriter(out));
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out, JsonConfiguration config) {
        return createGenerator(new OutputStreamWriter(out), config);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out, Charset charset) {
        return createGenerator(new OutputStreamWriter(out, charset));
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out, Charset charset, JsonConfiguration config) {
        return createGenerator(new OutputStreamWriter(out, charset), config);
    }

    @Override
    public JsonParser createParser(InputStream in) {
        return createParser(new InputStreamReader(in));
    }

    @Override
    public JsonParser createParser(InputStream in, Charset charset) {
        return createParser(new InputStreamReader(in, charset));
    }

    @Override
    public JsonParser createParser(InputStream in, JsonConfiguration config) {
        return createParser(new InputStreamReader(in), config);
    }

    @Override
    public JsonParser createParser(InputStream in, Charset charset, JsonConfiguration config) {
        return createParser(new InputStreamReader(in, charset), config);
    }

}
