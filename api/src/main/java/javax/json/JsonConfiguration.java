package javax.json;

import java.text.DateFormat;

public class JsonConfiguration {

    private DateFormat  dateFormat;

    private boolean prettyFormat = false;

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public boolean isPrettyFormat() {
        return prettyFormat;
    }

    public void setPrettyFormat(boolean prettyFormat) {
        this.prettyFormat = prettyFormat;
    }
}
