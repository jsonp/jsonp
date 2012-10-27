package javax.json;

public class JsonConfiguration {
    private String dateFormat;

    private boolean prettyFormat = false;

    public String getDateFormat() {
	return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
	this.dateFormat = dateFormat;
    }

    public boolean isPrettyFormat() {
	return prettyFormat;
    }

    public void setPrettyFormat(boolean prettyFormat) {
	this.prettyFormat = prettyFormat;
    }
}
