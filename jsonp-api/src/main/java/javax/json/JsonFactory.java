package javax.json;

import javax.json.spi.JsonProvider;


public class JsonFactory {
    public static JsonObject createJsonObject() {
        return JsonProvider.provider().createJsonObject();
    }
    
    public static JsonArray createJsonArray() {
        return JsonProvider.provider().createJsonArray();
    }
}
