package dev.codescreen.EventDriven;

/*
* Wrapper class to hold a request's result.
* */
public abstract class RequestResult {
    protected boolean success = false;
    protected String message;

    public boolean successfullyExecuted(){
        return success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
