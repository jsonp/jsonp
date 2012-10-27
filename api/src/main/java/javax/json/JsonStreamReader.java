package javax.json;

import java.io.Reader;
import java.util.Iterator;

import com.alibaba.json.JsonTokenizer;
import com.alibaba.json.JsonTokenizer.Token;


public class JsonStreamReader {

    private JsonTokenizer tokenizer;
    private EventIterator iterator;
    private Context       context;

    public JsonStreamReader(Reader reader){
        tokenizer = new JsonTokenizer(reader);
        iterator = new EventIterator();
    }

    public Iterator<Event> iterator() {
        return iterator;
    }

    public Context getContext() {
        return context;
    }

    class EventIterator implements Iterator<Event> {

        @Override
        public boolean hasNext() {
            return tokenizer.token() != Token.EOF;
        }

        @Override
        public Event next() {
            if (context != null && (context.event == Event.END_OBJECT || context.event == Event.END_ARRAY)) {
                context = context.parent;
                if (tokenizer.token() == Token.COMMA) {
                    tokenizer.nextToken();
                }
            }

            Token token = tokenizer.token();

            switch (token) {
                case LBRACE:
                    Context objSubContext = new Context(context, StructureType.Object, Event.START_OBJECT);
                    tokenizer.nextToken();
                    context = objSubContext;
                    return Event.START_OBJECT;
                case RBRACE:
                    tokenizer.nextToken();
                    return context.event = Event.END_OBJECT;
                case LBRACKET:
                    Context subArrayContext = new Context(context, StructureType.Array, Event.START_ARRAY);
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

        final Context       parent;
        final StructureType structureType;
        Event               event;
        Object              value;
        int                 level;

        public Context(Context parent, StructureType structureType, Event event){
            this.parent = parent;
            if (parent != null) {
                this.level = parent.level + 1;
            }
            this.structureType = structureType;
            this.event = event;
        }

        public int getLevel() {
            return level;
        }
    }

    public enum StructureType {
        Object, Array
    }

    /**
     * Event for parser state while parsing the JSON
     */
    public enum Event {
        /**
         * Event for start of a JSON array. This event indicates '[' is parsed.
         */
        START_ARRAY,
        /**
         * Event for start of a JSON object. This event indicates '{' is parsed.
         */
        START_OBJECT,
        /**
         * Event for a name in name(key)/value pair of a JSON object. This event indicates that the key name is parsed.
         * The name/key value itself can be accessed using {@link #getString}
         */
        KEY_NAME,
        /**
         * Event for JSON string value. This event indicates a string value in an array or object is parsed. The string
         * value itself can be accessed using {@link #getString}
         */
        VALUE_STRING,
        /**
         * Event for a number value. This event indicates a number value in an array or object is parsed. The number
         * value itself can be accessed using {@link javax.json.JsonNumber} methods
         */
        VALUE_NUMBER,
        /**
         * Event for a true value. This event indicates a true value in an array or object is parsed.
         */
        VALUE_TRUE,
        /**
         * Event for a false value. This event indicates a false value in an array or object is parsed.
         */
        VALUE_FALSE,
        /**
         * Event for a null value. This event indicates a null value in an array or object is parsed.
         */
        VALUE_NULL,
        /**
         * Event for end of an object. This event indicates '}' is parsed.
         */
        END_OBJECT,
        /**
         * Event for end of an array. This event indicates ']' is parsed.
         */
        END_ARRAY
    }
}
