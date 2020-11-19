package dev.codescreen.BusinessRules;

import dev.codescreen.Models.Account;
import dev.codescreen.Utils.AccountStatus;
import dev.codescreen.Utils.Rule;
import dev.codescreen.Utils.RuleEngine;

public class ValidAccountRule extends Rule {
    Account account;
    public ValidAccountRule(RuleEngine ruleEngine) {
        super(ruleEngine);
    }

    @Override
    public boolean when() {
        account = (Account)ruleEngine.getPattern(Account.class.getCanonicalName());
        return true; // always validate an account
    }

    @Override
    public void then() {
        // validate the account's status
        if (account.getAccountStatus().equals(AccountStatus.INACTIVE)){
            ruleEngine.addErrorMessage("Inactive account!");
        }
    }
}
