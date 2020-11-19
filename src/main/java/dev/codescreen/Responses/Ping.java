package dev.codescreen.Responses;

import java.time.LocalDateTime;

/*
* Response payload to GET /ping when server is healthy.
* */
public class Ping implements IResponse {

    LocalDateTime serverTime;

    public LocalDateTime getServerTime() {
        return serverTime;
    }

    public void setServerTime(LocalDateTime serverTime) {
        this.serverTime = serverTime;
    }

    public Ping(LocalDateTime serverTime) {
        this.serverTime = serverTime;
    }
}
