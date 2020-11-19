package dev.codescreen.Models;

import dev.codescreen.Requests.TransactionRequest;
import dev.codescreen.Utils.AccountStatus;

import java.util.ArrayList;
import java.util.List;

/*
* Hold details related to a user's account.
* Ideally, a Many:Many cardinality between Users and Accounts is maintained.
* This object must be as lightweight as possible.
* */
public class Account {
    private AccountStatus accountStatus;
    private AccountBalance accountBalance;
    private AccountConfiguration accountConfiguration;
    private List<TransactionRequest> transactionRequestList;

    // Current implementation
    private String accountId;

    public Account(String accountId) {
        this.accountId = accountId;
    }

    public Account(String accountId, AccountBalance accountBalance) {
        this.accountId = accountId;
        this.accountBalance = accountBalance;
        this.transactionRequestList = new ArrayList<>();
        setAccountStatus(AccountStatus.ACTIVE); // do we really want all New accounts to be active? Maybe need a New status.
    }

    public AccountBalance getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(AccountBalance accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public AccountConfiguration getAccountConfiguration() {
        return accountConfiguration;
    }

    public void setAccountConfiguration(AccountConfiguration accountConfiguration) {
        this.accountConfiguration = accountConfiguration;
    }

    public void addTransactionRequestToHistory(TransactionRequest transactionRequest){
        transactionRequestList.add(transactionRequest);
    }
}
