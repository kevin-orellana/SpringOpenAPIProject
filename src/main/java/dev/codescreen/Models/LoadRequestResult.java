package dev.codescreen.Models;

import dev.codescreen.EventDriven.RequestResult;
import dev.codescreen.Responses.LoadResponse;

public class LoadRequestResult extends RequestResult {
    private LoadResponse loadResponse;
    @Override
    public boolean successfullyExecuted() {
        return success;
    }

    public LoadRequestResult() {
    }

    public LoadResponse getLoadResponse() {
        return loadResponse;
    }

    public void setLoadResponse(LoadResponse loadResponse) {
        this.loadResponse = loadResponse;
    }
}
