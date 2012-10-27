package javax.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class JsonStructure {

    public static JsonObject parseJsonObject(String jsonString) {
	try {
	    JsonReader reader = new JsonReader(jsonString);
	    JsonObject jsonObject = reader.readJsonObject();
	    reader.close();
	    return jsonObject;
	} catch (IOException e) {
	    throw new JsonException(e);
	}
    }
    
    public static JsonArray parseJsonArray(String jsonString) {
	try {
	    JsonReader reader = new JsonReader(jsonString);
	    JsonArray jsonArray = reader.readJsonArray();
	    reader.close();
	    return jsonArray;
	} catch (IOException e) {
	    throw new JsonException(e);
	}
    }


    static boolean toBooleanValue(Object value, boolean defaultValue) {
	if (value == null) {
	    return defaultValue;
	}

	if (value instanceof Boolean) {
	    return ((Boolean) value).booleanValue();
	}

	if (value instanceof Number) {
	    int intValue = ((Number) value).intValue();
	    if (intValue == 1) {
		return true;
	    }

	    if (intValue == 0) {
		return false;
	    }
	} else if (value instanceof String) {
	    if ("true".equals(value)) {
		return true;
	    }

	    if ("false".equals(value)) {
		return true;
	    }

	    if ("1".equals(value)) {
		return true;
	    }

	    if ("0".equals(value)) {
		return true;
	    }
	}

	throw new ClassCastException("can not cast " + value.getClass()
		+ " to boolean");
    }

    static short toShortValue(Object value, short defaultValue) {
	if (value == null) {
	    return defaultValue;
	}

	if (value instanceof Number) {
	    return ((Number) value).shortValue();
	}

	if (value instanceof String) {
	    return Short.parseShort((String) value);
	}

	throw new ClassCastException("can not cast " + value.getClass()
		+ " to short");
    }

    static int toIntValue(Object value, int defaultValue) {
	if (value == null) {
	    return defaultValue;
	}

	if (value instanceof Number) {
	    return ((Number) value).intValue();
	}

	if (value instanceof String) {
	    return Integer.parseInt((String) value);
	}

	throw new ClassCastException("can not cast " + value.getClass()
		+ " to int");
    }

    static long toLongValue(Object value, long defaultValue) {
	if (value == null) {
	    return defaultValue;
	}

	if (value instanceof Number) {
	    return ((Number) value).longValue();
	}

	if (value instanceof String) {
	    return Long.parseLong((String) value);
	}

	throw new ClassCastException("can not cast " + value.getClass()
		+ " to long");
    }

    static BigDecimal toBigDecimal(Object value) {
	if (value == null) {
	    return null;
	}

	if (value instanceof BigDecimal) {
	    return (BigDecimal) value;
	}

	if (value instanceof BigInteger) {
	    return new BigDecimal((BigInteger) value);
	}

	if (value instanceof Number) {
	    return new BigDecimal(value.toString());
	}

	if (value instanceof String) {
	    String strVal = (String) value;
	    if (strVal.length() == 0) {
		return null;
	    }

	    return new BigDecimal(strVal);
	}

	throw new ClassCastException("can not cast " + value.getClass()
		+ " to BigDecimal");
    }
}
