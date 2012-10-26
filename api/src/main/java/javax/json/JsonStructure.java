package javax.json;

public class JsonStructure {

    static int toIntValue(Object value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        if (value instanceof Number) {
            return ((Integer) value).intValue();
        }

        if (value instanceof String) {
            return Integer.parseInt((String) value);
        }

        throw new ClassCastException("can not cast " + value.getClass() + " to int");
    }
    
    static long toLongValue(Object value, long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        
        if (value instanceof Number) {
            return ((Long) value).intValue();
        }
        
        if (value instanceof String) {
            return Long.parseLong((String) value);
        }
        
        throw new ClassCastException("can not cast " + value.getClass() + " to int");
    }
}
