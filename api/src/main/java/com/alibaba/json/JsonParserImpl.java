package com.alibaba.json;

import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Iterator;

import javax.json.JsonArray;
import javax.json.JsonConfiguration;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;

import com.alibaba.json.JsonTokenizer.Token;

public class JsonParserImpl implements JsonParser {

    private final JsonTokenizer tokenizer;
    private final JsonConfiguration config;

    private EventIterator iterator;

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

    @Override
    public Iterator<Event> iterator() {
	if (iterator == null) {
	    iterator = new EventIterator();
	}

	return iterator;
    }
    
    @Override
    public int getDepth() {
	return iterator.context.depth;
    }

    @Override
    public String getString() {
	Object value = iterator.context.value;

	if (value == null) {
	    return null;
	}

	if (value instanceof String) {
	    return (String) value;
	}

	return value.toString();
    }

    @Override
    public int getIntValue() {
	Object value = iterator.context.value;

	if (value == null) {
	    return 0;
	}
	
	if (value instanceof Integer) {
	    return (Integer) value;
	}

	if (value instanceof Number) {
	    return ((Number) value).intValue();
	}

	if (value instanceof String) {
	    return Integer.parseInt((String) value);
	}

	throw new JsonException("can not cast to int : " + value);
    }

    @Override
    public long getLongValue() {
	Object value = iterator.context.value;

	if (value == null) {
	    return 0;
	}

	if (value instanceof Long) {
	    return (Long) value;
	}

	if (value instanceof Number) {
	    return ((Number) value).longValue();
	}

	if (value instanceof String) {
	    return Long.parseLong((String) value);
	}

	throw new JsonException("can not cast to long : " + value);
    }

    @Override
    public BigDecimal getBigDecimalValue() {
	Object value = iterator.context.value;

	if (value == null) {
	    return null;
	}

	if (value instanceof BigDecimal) {
	    return (BigDecimal) value;
	}

	if (value instanceof Number || value instanceof String) {
	    return new BigDecimal(value.toString());
	}

	throw new JsonException("can not cast to int : " + value);
    }

    class EventIterator implements Iterator<Event> {
	private Context context;

	@Override
	public boolean hasNext() {
	    return tokenizer.token() != Token.EOF;
	}

	@Override
	public Event next() {
	    if (context != null
		    && (context.event == Event.END_OBJECT || context.event == Event.END_ARRAY)) {
		context = context.parent;
		if (tokenizer.token() == Token.COMMA) {
		    tokenizer.nextToken();
		}
	    }

	    Token token = tokenizer.token();

	    switch (token) {
	    case LBRACE:
		Context objSubContext = new Context(context,
			StructureType.Object, Event.START_OBJECT);
		tokenizer.nextToken();
		context = objSubContext;
		return Event.START_OBJECT;
	    case RBRACE:
		tokenizer.nextToken();
		return context.event = Event.END_OBJECT;
	    case LBRACKET:
		Context subArrayContext = new Context(context,
			StructureType.Array, Event.START_ARRAY);
		tokenizer.nextToken();
		context = subArrayContext;
		return Event.START_ARRAY;
	    case RBRACKET:
		tokenizer.nextToken();
		return context.event = Event.END_ARRAY;
	    case TRUE:
		if (context != null) {
		    context.value = true;
		    tokenizer.nextToken();
		    if (tokenizer.token() == Token.COMMA) {
			tokenizer.nextToken();
		    }
		} else {
		    tokenizer.nextToken();
		}
		return context.event = Event.VALUE_TRUE;
	    case FALSE:
		if (context != null) {
		    context.value = false;
		    tokenizer.nextToken();
		    if (tokenizer.token() == Token.COMMA) {
			tokenizer.nextToken();
		    }
		} else {
		    tokenizer.nextToken();
		}
		return context.event = Event.VALUE_FALSE;
	    case NULL:
		if (context != null) {
		    context.value = null;
		    tokenizer.nextToken();
		    if (tokenizer.token() == Token.COMMA) {
			tokenizer.nextToken();
		    }
		} else {
		    tokenizer.nextToken();
		}
		return context.event = Event.VALUE_NULL;
	    case STRING:
		if (context != null) {
		    if (context.structureType == StructureType.Object) {
			switch (context.event) {
			case START_OBJECT:
			case VALUE_FALSE:
			case VALUE_NUMBER:
			case VALUE_NULL:
			case VALUE_STRING:
			case VALUE_TRUE:
			    tokenizer.nextToken();
			    context.event = Event.KEY_NAME;
			    context.value = tokenizer.stringValue();
			    tokenizer.accept(Token.COLON);
			    return context.event;
			case KEY_NAME:
			    context.value = tokenizer.stringValue();
			    tokenizer.nextToken();
			    if (tokenizer.token() == Token.COMMA) {
				tokenizer.nextToken();
			    }
			    return context.event = Event.VALUE_STRING;
			default:
			    throw new JsonException("syntax error");
			}
		    } else {
			context.value = tokenizer.stringValue();
			tokenizer.nextToken();
			if (tokenizer.token() == Token.COMMA) {
			    tokenizer.nextToken();
			}
			return context.event = Event.VALUE_STRING;
		    }
		}
	    case INT:
		if (context != null) {
		    context.value = tokenizer.longValue();
		    tokenizer.nextToken();
		    if (tokenizer.token() == Token.COMMA) {
			tokenizer.nextToken();
		    }
		} else {
		    tokenizer.nextToken();
		}
		return context.event = Event.VALUE_NUMBER;
	    case DOUBLE:
		if (context != null) {
		    context.value = tokenizer.doubleValue();
		    tokenizer.nextToken();
		    if (tokenizer.token() == Token.COMMA) {
			tokenizer.nextToken();
		    }
		} else {
		    tokenizer.nextToken();
		}
		return context.event = Event.VALUE_NUMBER;
	    case EOF:
		return null;
	    default:
		return null;
	    }
	}

	@Override
	public void remove() {
	    throw new UnsupportedOperationException();
	}
    }

    public static class Context {

	final Context parent;
	final StructureType structureType;
	Event event;
	Object value;
	int depth;

	public Context(Context parent, StructureType structureType, Event event) {
	    this.parent = parent;
	    if (parent != null) {
		this.depth = parent.depth + 1;
	    }
	    this.structureType = structureType;
	    this.event = event;
	}

	public int getLevel() {
	    return depth;
	}
    }

    public enum StructureType {
	Object, Array
    }
}
