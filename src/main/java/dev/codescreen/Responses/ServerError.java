package dev.codescreen.Responses;

/*
* * Response payload to GET /ping when server is UNHEALTHY.
 * */
public class ServerError implements IResponse {

    String message;
    String code;

    public ServerError(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
