package javax.json;

import java.util.Properties;


public class JsonConfiguration {
    public static JsonConfiguration defaultConfig = new JsonConfiguration();
    
    private Properties properties = new Properties();

    public JsonConfiguration() {
        this (new Properties());
    }
    
    public JsonConfiguration(Properties properties) {
        this.properties = properties;
    }
    
    
    
    public Object get(String name) {
        return properties.getProperty(name);
    }
    
    public Object put(String name, Object value) {
        return properties.put(name, value);
    }
}
