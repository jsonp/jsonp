package javax.json.stream;

import java.io.Closeable;

import javax.json.JsonArray;
import javax.json.JsonObject;


public interface JsonParser extends Closeable {
    Object read();
    
    JsonObject readJsonObject();
    
    JsonArray readJsonArray();
}
