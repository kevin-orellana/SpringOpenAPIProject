package dev.codescreen.Requests;

import dev.codescreen.Models.TransactionAmount;

public class AuthorizationRequest extends TransactionRequest {

    public AuthorizationRequest(String messageId, String userId, TransactionAmount transactionAmount) {
        super(messageId, userId, transactionAmount);
    }
}
