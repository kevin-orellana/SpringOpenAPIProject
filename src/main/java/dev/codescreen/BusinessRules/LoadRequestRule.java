package dev.codescreen.BusinessRules;

import dev.codescreen.Models.Account;
import dev.codescreen.Models.AccountBalance;
import dev.codescreen.Models.AccountConfiguration;
import dev.codescreen.Models.TransactionAmount;
import dev.codescreen.Requests.LoadRequest;
import dev.codescreen.Utils.Rule;
import dev.codescreen.Utils.RuleEngine;

import java.math.BigDecimal;
import java.util.Optional;

public class LoadRequestRule extends Rule {
    Account account;
    LoadRequest loadRequest;
    public LoadRequestRule(RuleEngine ruleEngine) {
        super(ruleEngine);
    }

    @Override
    public boolean when() {
        account = (Account)ruleEngine.getPattern(Account.class.getCanonicalName());
        loadRequest = (LoadRequest)ruleEngine.getPattern(LoadRequest.class.getCanonicalName());

        // for now, we always validate a load request - this can be configurable as needed
        return true;
    }

    @Override
    public void then() {
        AccountBalance accountBalance = account.getAccountBalance();
        TransactionAmount loadRequestTxAmount = loadRequest.getTransactionAmount();
        if (accountBalance == null || loadRequestTxAmount == null){
            ruleEngine.addErrorMessage("Account balance or load request transaction amount may not be null");
            return;
        }

        BigDecimal currentBalanceAmount = accountBalance.getAmount();
        BigDecimal additiveAmount = loadRequestTxAmount.getAmount();

        BigDecimal newAmount = currentBalanceAmount.add(additiveAmount);

        // can someone have too much money?.. this rule can be used to trigger notification events..
        // e.g. GovernmentEntityAlertingService.alert(account, Compliance.OVER_10000_USD)
    }
}
