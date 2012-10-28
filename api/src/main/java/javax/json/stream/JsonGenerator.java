package javax.json.stream;

import java.io.Closeable;
import java.io.Flushable;
import java.math.BigDecimal;
import java.util.Date;

import javax.json.JsonArray;
import javax.json.JsonObject;

public interface JsonGenerator extends Closeable, Flushable {
    JsonGenerator writeJsonObject(JsonObject jsonObject);

    JsonGenerator writeJsonArray(JsonArray jsonArray);
    
    JsonGenerator writeAny(Object o);

    JsonGenerator beginObject();

    JsonGenerator endObject();

    JsonGenerator beginArray();

    JsonGenerator endArray();

    JsonGenerator writeKey(String key);

    JsonGenerator writeKeyValue(String key, Object value);

    JsonGenerator writeNull();

    JsonGenerator writeBoolean(boolean value);

    JsonGenerator writeInt(int i);

    JsonGenerator writeLong(long i);

    JsonGenerator writeBigDecimal(BigDecimal value);

    JsonGenerator writeString(String value);

    JsonGenerator writeDate(Date date);
}
