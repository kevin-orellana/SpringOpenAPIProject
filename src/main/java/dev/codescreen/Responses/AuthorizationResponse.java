package dev.codescreen.Responses;

import dev.codescreen.Models.AccountBalance;

/*
* AuthorizationResponse:
      type: object
      additionalProperties: false
      description: The result of an authorization
      required:
        - userId
        - messageId
        - responseCode
        - balance
      properties:
        userId:
          type: string
          minLength: 1
        messageId:
          type: string
          minLength: 1
        responseCode:

          $ref: '#/components/schemas/ResponseCode'
        balance:
          $ref: '#/components/schemas/Amount'
*
* */
public class AuthorizationResponse implements IResponse {
    private String userId;
    private String messageId;
    private ResponseCode responseCode;
    private AccountBalance accountBalance;

    public AuthorizationResponse(String userId, String messageId, ResponseCode responseCode, AccountBalance accountBalance) {
        this.userId = userId;
        this.messageId = messageId;
        this.responseCode = responseCode;
        this.accountBalance = accountBalance;
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

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public AccountBalance getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(AccountBalance accountBalance) {
        this.accountBalance = accountBalance;
    }
}
