package dev.codescreen.BusinessRules;

import dev.codescreen.Models.Account;
import dev.codescreen.Models.AccountBalance;
import dev.codescreen.Models.AccountConfiguration;
import dev.codescreen.Models.TransactionAmount;
import dev.codescreen.Requests.AuthorizationRequest;
import dev.codescreen.Requests.TransactionRequest;
import dev.codescreen.Utils.Rule;
import dev.codescreen.Utils.RuleEngine;

import java.util.Optional;

public class TransactionalRequestRule extends Rule {
    Account account;
    TransactionRequest transactionRequest;
    public TransactionalRequestRule(RuleEngine ruleEngine) {
        super(ruleEngine);
    }

    @Override
    public boolean when() {
        account = (Account)ruleEngine.getPattern(Account.class.getCanonicalName());
        transactionRequest = (TransactionRequest)ruleEngine.getPattern(TransactionRequest.class.getCanonicalName());
        // always want to perform this check!
        return true;
    }

    @Override
    public void then() {
        AccountBalance accountBalance = account.getAccountBalance();
        String accountBalanceCurrency = accountBalance.getCurrency();

        TransactionAmount transactionAmount = transactionRequest.getTransactionAmount();
        String transactionCurrency = transactionAmount.getCurrency();

    }
}
