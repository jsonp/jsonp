package javax.json.stream;

import java.io.Closeable;
import java.math.BigDecimal;
import java.util.Iterator;

import javax.json.JsonArray;
import javax.json.JsonObject;

public interface JsonParser extends Closeable {
    Object read();

    JsonObject readJsonObject();

    JsonArray readJsonArray();
    
    int getDepth();
    
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
         * Event for a name in name(key)/value pair of a JSON object. This event
         * indicates that the key name is parsed. The name/key value itself
         * can be accessed using {@link #getString}
         */
        KEY_NAME,
        /**
         * Event for JSON string value. This event indicates a string value in
         * an array or object is parsed. The string value itself can be
         * accessed using {@link #getString}
         */
        VALUE_STRING,
        /**
         * Event for a number value. This event indicates a number value in
         * an array or object is parsed. The number value itself can be
         * accessed using {@link javax.json.JsonNumber} methods
         */
        VALUE_NUMBER,
        /**
         * Event for a true value. This event indicates a true value in an
         * array or object is parsed.
         */
        VALUE_TRUE,
        /**
         * Event for a false value. This event indicates a false value in an
         * array or object is parsed.
         */
        VALUE_FALSE,
        /**
         * Event for a null value. This event indicates a null value in an
         * array or object is parsed.
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
    
    Iterator<Event> iterator();

    /**
     * Returns a String for name(key), string value and number value. This
     * method is only called when the parser state is one of
     * {@link Event#KEY_NAME}, {@link Event#VALUE_STRING},
     * {@link Event#VALUE_NUMBER}.
     * 
     * @return name when the parser state is {@link Event#KEY_NAME}. string
     *         value when the parser state is {@link Event#VALUE_STRING}. number
     *         value when the parser state is {@link Event#VALUE_NUMBER}.
     * @throws IllegalStateException
     *             when the parser is not in one of KEY_NAME, VALUE_STRING,
     *             VALUE_NUMBER states
     */
    String getString();

    /**
     * Returns JSON number as an integer. The returned value is equal to
     * {@code new BigDecimal(getString()).intValue()}. Note that this conversion
     * can lose information about the overall magnitude and precision of the
     * number value as well as return a result with the opposite sign. This
     * method is only called when the parser is in {@link Event#VALUE_NUMBER}
     * state.
     * 
     * @return an integer for JSON number.
     * @throws IllegalStateException
     *             when the parser state is not VALUE_NUMBER
     * @see java.math.BigDecimal#intValue()
     */
    int getIntValue();

    /**
     * Returns JSON number as a long. The returned value is equal to
     * {@code new BigDecimal(getString()).longValue()}. Note that this
     * conversion can lose information about the overall magnitude and precision
     * of the number value as well as return a result with the opposite sign.
     * This method is only called when the parser is in
     * {@link Event#VALUE_NUMBER} state.
     * 
     * @return a long for JSON number.
     * @throws IllegalStateException
     *             when the parser state is not VALUE_NUMBER
     * @see java.math.BigDecimal#longValue()
     */
    long getLongValue();

    /**
     * Returns JSON number as a {@code BigDecimal}. The BigDecimal is created
     * using {@code new BigDecimal(getString())}. This method is only called
     * when the parser is in {@link Event#VALUE_NUMBER} state.
     * 
     * @return a BigDecimal for JSON number
     * @throws IllegalStateException
     *             when the parser state is not VALUE_NUMBER
     */
    BigDecimal getBigDecimalValue();
}
