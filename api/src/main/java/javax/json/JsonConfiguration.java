package javax.json;

import java.util.Properties;


public class JsonConfiguration {
    private Properties properties = new Properties();

    public JsonConfiguration() {
        
    }
    
    public Object get(String name) {
        return properties.getProperty(name);
    }
    
    public Object put(String name, Object value) {
        return properties.put(name, value);
    }
}
