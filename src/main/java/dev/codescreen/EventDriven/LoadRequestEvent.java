package dev.codescreen.EventDriven;

import dev.codescreen.Requests.LoadRequest;


public class LoadRequestEvent extends Event {
    final static public String TYPE = "LoadRequestEvent";

    final private LoadRequest loadRequest;  // event transactional data holder

    public LoadRequestEvent(LoadRequest loadRequest, EventStatus eventStatus) {
        super(loadRequest, eventStatus);
        this.loadRequest = loadRequest;
    }

    // signal to the event-processing service to process LoadRequestEvents synchronously
    @Override
    public boolean processSynchronously() {
        return true;
    }

    public LoadRequest getLoadRequest() {
        return loadRequest;
    }

    @Override public String getType() {
        return TYPE;
    }
}
