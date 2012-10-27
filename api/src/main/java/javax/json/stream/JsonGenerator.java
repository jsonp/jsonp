package javax.json.stream;

import java.io.Closeable;
import java.io.Flushable;
import java.util.Date;

import javax.json.JsonArray;
import javax.json.JsonObject;

public interface JsonGenerator extends Closeable, Flushable {
    JsonGenerator writeObject(JsonObject jsonObject);
    JsonGenerator writeArray(JsonArray jsonArray);
    
    JsonGenerator writeDate(Date date);

    JsonGenerator writeBeginObject();
    JsonGenerator writeEndObject();
    
    JsonGenerator writeKeyValue(Object key, Object value);
    JsonGenerator writeKeyValueSeperator();
    
    JsonGenerator writeNull();
}
