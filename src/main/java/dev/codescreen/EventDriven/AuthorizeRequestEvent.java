package dev.codescreen.EventDriven;

import dev.codescreen.Requests.AuthorizationRequest;

public class AuthorizeRequestEvent extends Event {
    public final static String TYPE = "AuthorizeRequestEvent";

    private AuthorizationRequest authorizationRequest;

    public AuthorizeRequestEvent(AuthorizationRequest authorizationRequest, EventStatus eventStatus) {
        super(authorizationRequest, eventStatus);
        this.authorizationRequest = authorizationRequest;
    }

    public AuthorizationRequest getAuthorizationRequest() {
        return authorizationRequest;
    }

    public void setAuthorizationRequest(AuthorizationRequest authorizationRequest) {
        this.authorizationRequest = authorizationRequest;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
