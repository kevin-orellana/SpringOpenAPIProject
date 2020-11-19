package dev.codescreen.Utils.Exceptions;

public class AccountAlreadyExist extends RuntimeException {
    @Override
    public String getMessage() {
        return "AccountAlreadyExist";
    }
}
