package dev.codescreen.Requests;

import dev.codescreen.Models.TransactionAmount;

/*
* Holds the information required to process a LoadRequest.
* */
public class LoadRequest extends TransactionRequest {
    // additional LoadRequest members

    public LoadRequest(String messageId, String userId, TransactionAmount transactionAmount) {
        super(messageId, userId, transactionAmount);
    }
}
