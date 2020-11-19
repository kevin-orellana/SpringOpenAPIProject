package dev.codescreen.Responses;

import dev.codescreen.Models.AccountBalance;

public class LoadResponse implements IResponse {

    private String userId;
    private String messageId;
    AccountBalance balance;

    public LoadResponse(String userId) {
        this.userId = userId;
    }

    public LoadResponse(AccountBalance balance) {
        this.balance = balance;
    }

    public LoadResponse(String userId, String messageId, AccountBalance balance) {
        this.userId = userId;
        this.messageId = messageId;
        this.balance = balance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public AccountBalance getBalance() {
        return balance;
    }

    public void setBalance(AccountBalance balance) {
        this.balance = balance;
    }
}
