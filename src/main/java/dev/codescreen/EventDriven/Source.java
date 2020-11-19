package dev.codescreen.EventDriven;

/*
* Hold details about where this event originated from (for example, a REST call)
* */
public class Source {
    public String sourceName;

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
