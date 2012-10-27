package com.alibaba.json;

import java.io.Reader;
import java.io.StringReader;

import javax.json.JsonArray;
import javax.json.JsonConfiguration;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;

import com.alibaba.json.JsonTokenizer.Token;

public class JsonParserImpl implements JsonParser {

    private final JsonTokenizer tokenizer;
    private final JsonConfiguration config;

    /**
     * Creates a JSON reader from a character stream
     * 
     * @param reader
     *            a reader from which JSON is to be read
     * @return a JSON reader
     */
    public JsonParserImpl(Reader reader, JsonConfiguration config) {
	this.tokenizer = new JsonTokenizer(reader);
	this.config = config;
    }

    public JsonParserImpl(String text) {
	this(new StringReader(text), new JsonConfiguration());
    }

    public JsonConfiguration getConfig() {
	return config;
    }

    public Object read() {
	if (tokenizer.token() == Token.LBRACE) {
	    return readJsonObject();
	}

	Token token = tokenizer.token();

	if (token == Token.INT) {
	    Object value;

	    if (tokenizer.longValue() >= Integer.MIN_VALUE
		    && tokenizer.longValue() <= Integer.MAX_VALUE) {
		value = (int) tokenizer.longValue();
	    } else {
		value = tokenizer.longValue();
	    }

	    tokenizer.nextToken();
	    return value;
	}

	if (token == Token.DOUBLE) {
	    Object value = tokenizer.doubleValue();
	    tokenizer.nextToken();
	    return value;
	}

	if (token == Token.STRING) {
	    Object value = tokenizer.stringValue();
	    tokenizer.nextToken();
	    return value;
	}

	if (token == Token.LBRACKET) {
	    return readJsonArray();
	}

	if (token == Token.TRUE) {
	    tokenizer.nextToken();
	    return true;
	}

	if (token == Token.FALSE) {
	    tokenizer.nextToken();
	    return false;
	}

	if (token == Token.NULL) {
	    tokenizer.nextToken();
	    return null;
	}

	throw new IllegalArgumentException("illegal token : " + token);
    }

    public JsonArray readJsonArray() {
	tokenizer.accept(Token.LBRACKET);
	JsonArray array = new JsonArray();

	for (;;) {
	    Token token = tokenizer.token();

	    if (token == Token.RBRACKET) {
		break;
	    }

	    if (token == Token.COMMA) {
		tokenizer.nextToken();
		continue;
	    }

	    Object item = read();
	    array.add(item);
	}

	tokenizer.accept(Token.RBRACKET);
	return array;
    }

    /**
     * Returns a JSON array or object that is represented in the character
     * stream. This method needs to be called only once for a reader instance.
     * 
     * @return a {@link JsonArray} or {@code JsonObject}
     * @throws JsonException
     *             if a JsonObject or JsonArray cannot be created due to i/o
     *             error or incorrect representation
     * @throws IllegalStateException
     *             if this method or close method is already called
     */
    public JsonObject readJsonObject() {
	tokenizer.accept(Token.LBRACE);
	JsonObject map = new JsonObject();

	for (;;) {
	    Token token = tokenizer.token();

	    if (token == Token.RBRACE) {
		break;
	    }

	    if (token == Token.COMMA) {
		tokenizer.nextToken();
		continue;
	    }

	    String key;
	    {
		if (token != Token.STRING) {
		    throw new IllegalArgumentException("illegal json token : "
			    + token);
		}
		key = tokenizer.stringValue();
		tokenizer.nextToken();
	    }

	    tokenizer.accept(Token.COLON);

	    Object value = read();

	    map.put(key, value);
	}

	tokenizer.accept(Token.RBRACE);
	return map;
    }

    /**
     * Closes this reader and frees any resources associated with the reader.
     * This doesn't close the underlying input source.
     */
    @Override
    public void close() {
	this.tokenizer.close();
    }

}
