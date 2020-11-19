package dev.codescreen.Models;

import dev.codescreen.EventDriven.RequestResult;
import dev.codescreen.Responses.AuthorizationResponse;

public class AuthorizationRequestResult extends RequestResult {
    private AuthorizationResponse authorizationResponse;

    public AuthorizationResponse getAuthorizationResponse() {
        return authorizationResponse;
    }

    public void setAuthorizationResponse(AuthorizationResponse authorizationResponse) {
        this.authorizationResponse = authorizationResponse;
    }
}
