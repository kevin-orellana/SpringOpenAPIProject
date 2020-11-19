package dev.codescreen.BusinessRules;

import dev.codescreen.Models.Account;
import dev.codescreen.Models.AccountBalance;
import dev.codescreen.Models.AccountConfiguration;
import dev.codescreen.Models.TransactionAmount;
import dev.codescreen.Requests.AuthorizationRequest;
import dev.codescreen.Utils.Rule;
import dev.codescreen.Utils.RuleEngine;

import java.math.BigDecimal;
import java.util.Optional;

public class AuthorizationRequestRule extends TransactionalRequestRule {
    AuthorizationRequest authorizationRequest;
    public AuthorizationRequestRule(RuleEngine ruleEngine) {
        super(ruleEngine);
    }

    @Override
    public boolean when() {
        account = (Account)ruleEngine.getPattern(Account.class.getCanonicalName());
        authorizationRequest = (AuthorizationRequest)ruleEngine.getPattern(AuthorizationRequest.class.getCanonicalName());

        // for now, we always validate a load request - this can be configurable as needed
        return true;
    }

    @Override
    public void then() {
        AccountBalance accountBalance = account.getAccountBalance();
        TransactionAmount loadRequestTxAmount = authorizationRequest.getTransactionAmount();
        if (accountBalance == null || loadRequestTxAmount == null){
            ruleEngine.addErrorMessage("Account balance or authoriization request transaction amount may not be null");
            return;
        }

        BigDecimal currentBalanceAmount = accountBalance.getAmount();
        BigDecimal subtractiveAmount = loadRequestTxAmount.getAmount();
        BigDecimal newAmount = currentBalanceAmount.subtract(subtractiveAmount);

        if (newAmount.compareTo(BigDecimal.ZERO) < 0){
            // can someone have too little money?... what if an account's config does not allow this?
            Optional<AccountConfiguration> accountConfigOptional = Optional.ofNullable(account.getAccountConfiguration());
            if (accountConfigOptional.isPresent()){
                accountConfigOptional.ifPresent(accountConfig -> {
                    if (!accountConfig.allowsOverdrafts()){
                        ruleEngine.addErrorMessage("This account does not allow overdrafts by account configuration!");
                    }
                });
            } else {
                ruleEngine.addErrorMessage("This account does not allow overdrafts by default!");

            }


        }
    }
}
